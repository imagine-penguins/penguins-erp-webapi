package com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.LeaveRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveHistoryDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestUpdateDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.document.UserDocumentStoreRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestMapper leaveRequestMapper;
    private final UserService userService;
    private final IAuthenticationFacade authenticationFacade;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestService leaveRequestService;
    private final InstituteDepartmentRepository departmentRepository;
    private final UserDocumentStoreRepository userDocumentStoreRepository;

    @PostMapping
    public ResponseEntity<String> saveLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        log.trace("Request for new leave application. Processing...");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        //Validate if leave already taken for that day.
        if (!leaveRequestService.isOnLeave(userContext.getUserId(), leaveRequestDTO.getStartDate()
                , leaveRequestDTO.getEndDate()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Leave Already Applied for the given period.");
        }
        if (leaveRequestDTO.getEndDate().isBefore(leaveRequestDTO.getStartDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date cannot be after end date.");
        }
        if (leaveRequestDTO.getEndDate().equals(leaveRequestDTO.getStartDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date cannot be equal to end date.");
        }
        /*if (leaveRequestDTO.getStartDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date time cannot be before to Now.");
        }*/
        User user = userService
                .findById(userContext.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));

        if (leaveRequestDTO.getApprovesId() == null) {
            if (user instanceof Employee) {
                if (((Employee) user).getManager() == null) {
                    leaveRequestDTO.setApprovesId(userService
                            .findByDepartmentId(
                                    departmentRepository.findByDepartmentName("ADMIN").get(0).getId()
                            ).get(0).getId());
                }
                else {leaveRequestDTO.setApprovesId(((Employee) user).getManager().getId());}
            } else if (user instanceof Student) {
                if (((Student) user).getInstituteClassSection() == null
                        || ((Student) user).getInstituteClassSection().getTeacher() == null) {
                    leaveRequestDTO.setApprovesId(userService
                            .findByDepartmentId(
                                    departmentRepository.findByDepartmentName("ADMIN").get(0).getId()
                            ).get(0).getId());
                }else {
                    leaveRequestDTO.setApprovesId(((Student) user).getInstituteClassSection().getTeacher().getId());
                }
            } else {
                leaveRequestDTO.setApprovesId(userService
                        .findByDepartmentId(
                                departmentRepository.findByDepartmentName("ADMIN").get(0).getId()
                        ).get(0).getId());
            }
        }

        LeaveRequest newLeaveRequest = leaveRequestMapper.dtoToEntity(leaveRequestDTO);

        user.setLeaveRequests(newLeaveRequest);

        User approves = userService
                .findById(leaveRequestDTO.getApprovesId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        approves.setLeaveRequestsApprover(newLeaveRequest);

        newLeaveRequest.setLeaveRequestStatus(LeaveRequestStatus.PENDING);
        log.debug("Saving to database");
        LeaveRequest savedLeaveRequest = leaveRequestService
                .saveLeaveRequest(newLeaveRequest);
        log.debug("Saved to database.");
        log.trace("Request for new leave application. Finished...");
        return ResponseEntity.status(HttpStatus.CREATED).body("Leave Request saved.");
    }

    @GetMapping
    public PagedModel<LeaveResponseDTO> all(
            @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id,desc") String[] sort
            , @RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
            , @RequestParam Optional<Period> period
            , @RequestParam Optional<String> value
    ) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, SortingService.sort(sort));
        final Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);

        List<LeaveResponseDTO> responseDTOS = new ArrayList<>();
        Specification<LeaveRequest> leaveRequestSpecification
                = LeaveRequestSpecification.filterLeaveRequests(searchMap);
        log.debug("view received leaves");
        leaveRequestSpecification = leaveRequestSpecification
                .and(LeaveRequestSpecification.leaveRequestByUserId(userContext.getUserId()));
        log.debug("Calling DB.");
        Page<LeaveRequest> leaveRequestPage = leaveRequestService
                .findAllBySpecification(leaveRequestSpecification, pageable);
        log.debug("DB call finished.");
        leaveRequestPage
                .get()
                .map(leaveRequestMapper::entityToDTO)
                .map(dto -> {
                    UserDocumentStore docStore = userDocumentStoreRepository
                            .findByUserIdAndDocumentType(dto.getUserId(), UserDocumentType.DISPLAY_PICTURE);
                    if (docStore != null)
                        dto.setProfilePic(docStore.getStoreURL());
                    return dto;
                })
                .map(leaveResponseDTO -> leaveResponseDTO.add(
                        linkTo(methodOn(LeaveRequestController.class)
                                .updateLeaveRequestStatus(leaveResponseDTO.getId(), null, null))
                                .withRel(PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS.getPrivilegeCode())))
                .forEach(responseDTOS::add);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, leaveRequestPage.getTotalElements()
                , leaveRequestPage.getTotalPages());
        return PagedModel.of(responseDTOS, pageMetadata, linkTo(methodOn(LeaveRequestController.class).leaveRequestHistory(
                search, sort, page, size, period, value
        )).withSelfRel());
    }

    /*@GetMapping
    public LeaveResponseDTO one(Long leaveRequestId) {
        return null;
    }
*/
    @PutMapping("/{leave-request-id}")
    public ResponseEntity<String> updateLeaveRequest(@PathVariable("leave-request-id") Long leaveRequestId
            , @RequestBody LeaveRequestUpdateDTO dto) {
        LeaveRequest oldLeaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave Request Not Found With the provided Id"));
        leaveRequestMapper.dtoToEntity(oldLeaveRequest, dto);
        LeaveRequest newLeaveRequest = leaveRequestRepository.save(oldLeaveRequest);
        if (newLeaveRequest != null)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Leave Updated.");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Leave not Updated.");
    }

    @PutMapping("/{leave-request-id}/status/{status}")
    public ResponseEntity<String> updateLeaveRequestStatus(@PathVariable("leave-request-id") Long leaveRequestId
            , @PathVariable("status") LeaveRequestStatus status
            , @RequestParam("reason") Optional<String> reason) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        LeaveRequest oldLeaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave Request Not Found With the provided Id"));
        if (status == LeaveRequestStatus.REJECTED) {
            oldLeaveRequest
                    .setRejectedReason(
                            reason.orElseThrow(() -> new RuntimeException("For rejected request reason is required.")));
        }
        oldLeaveRequest.setLeaveRequestStatus(status);
        oldLeaveRequest.setApprovedBy(
                userService
                        .findById(userContext.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()))
        );
        leaveRequestRepository.save(oldLeaveRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Status Updated.");

    }

    @GetMapping("/history")
    public PagedModel<LeaveResponseDTO> leaveRequestHistory(
            @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id,desc") String[] sort
            , @RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value
    ) {
        log.trace("leave history started...");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, SortingService.sort(sort));
        final Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);
        Optional<LocalDateTime> startDate = period
                .map(p -> FilterService.periodStartDateValue(p, value))
                .orElse(Optional.empty());
        Optional<LocalDateTime> endDate = period
                .map(p -> FilterService.periodEndDateValue(p, value))
                .orElse(Optional.empty());
        List<LeaveResponseDTO> responseDTOS = new ArrayList<>();
        Specification<LeaveRequest> leaveRequestSpecification = LeaveRequestSpecification.filterLeaveRequests(searchMap);
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.VIEW_RECEIVED_LEAVE_REQUEST.getPrivilegeCode())
        )) {
            log.debug("view received leaves");
            leaveRequestSpecification = leaveRequestSpecification
                    .and(LeaveRequestSpecification.leaveRequestByApprovesId(userContext.getUserId()));
        }
        List<SearchCriteria> searchCriteria = searchMap.get("requestStatus");
        if (
                searchMap.containsKey("requestStatus") &&
                        searchCriteria.get(0).getValue().toString().equalsIgnoreCase("P")
        ) {
            if (searchCriteria.get(0).getOperation() == SearchOperation.NOT_EQUAL) {
                leaveRequestSpecification = leaveRequestSpecification
                        .and(LeaveRequestSpecification.leaveRequestByApprovedBy(userContext.getUserId()));
            } else {
                leaveRequestSpecification = leaveRequestSpecification
                        .and(new GenericSpecification<>(
                                new SearchCriteria("leaveRequestStatus", LeaveRequestStatus.PENDING, SearchOperation.EQUAL)
                        ));
            }
        }
        leaveRequestSpecification = leaveRequestSpecification.and(LeaveRequestSpecification.leaveRequestByNotUserId(userContext.getUserId()));
        log.debug("Calling Database.");
        Page<LeaveRequest> leaveRequestPage = leaveRequestService.findAllBySpecification(leaveRequestSpecification, pageable);
        log.debug("Database call finished");
        leaveRequestPage
                .get()
                .map(leaveRequestMapper::entityToDTO)
                .map(leaveResponseDTO -> leaveResponseDTO.add(
                        linkTo(methodOn(LeaveRequestController.class)
                                .updateLeaveRequestStatus(leaveResponseDTO.getId(), null, null))
                                .withRel(PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS.getPrivilegeCode())))
                .map(dto -> {
                    dto.setProfilePic(
                            userDocumentStoreRepository
                                    .findByUserIdAndDocumentType(dto.getUserId(), UserDocumentType.DISPLAY_PICTURE)
                            .getStoreURL()
                    );
                    return dto;
                })
                .forEach(responseDTOS::add);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page
                , leaveRequestPage.getTotalElements(), leaveRequestPage.getTotalPages());
        return PagedModel.of(responseDTOS, pageMetadata, linkTo(methodOn(LeaveRequestController.class).leaveRequestHistory(
                search, sort, page, size, period, value)).withSelfRel());
    }

    @GetMapping("/history/graph")
    public CollectionModel<LeaveHistoryDTO.GraphData> receivedLeaveGraph(@RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "M") Period period) {
        log.debug("/leave-requests/history/graph");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();

        Specification<LeaveRequest> specification = LeaveRequestSpecification
                .leaveRequestByApprovesId(userContext.getUserId());

        Map<String, List<SearchCriteria>> searchCriteriaMap = FilterService.createSearchMap(search);
        specification = specification.and(LeaveRequestSpecification.filterLeaveRequests(searchCriteriaMap));

        specification = specification.and(LeaveRequestSpecification.leaveRequestByNotUserId(userContext.getUserId()));

        log.debug("Calling DB.");
        List<LeaveRequest> leaveRequests = leaveRequestService.findAllBySpecification(specification);
        log.debug("DB Call finished");
        List<LocalDateTime> allLeavesDates = new ArrayList<>();
        leaveRequests.stream().map(leaveRequestMapper::getLeavesDates).forEach(allLeavesDates::addAll);

        List<LeaveHistoryDTO.GraphData> graphData = leaveRequestMapper
                .getLeaveCount(allLeavesDates, period)
                .entrySet()
                .stream()
                .map(leaveRequestMapper::getGraphDataFromMapEntry)
                .collect(Collectors.toList());

        return CollectionModel.of(graphData);
    }

    @GetMapping("/graph")
    public CollectionModel<LeaveHistoryDTO.GraphData> appliedLeaveGraph(@RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "M") Period period) {
        log.debug("/leave-requests/graph");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Map<String, List<SearchCriteria>> searchCriteriaMap = FilterService.createSearchMap(search);

        Specification<LeaveRequest> specification = LeaveRequestSpecification.leaveRequestByUserId(userContext.getUserId());

        specification = specification.and(LeaveRequestSpecification.filterLeaveRequests(searchCriteriaMap));

        List<LeaveRequest> leaveRequests = leaveRequestService.findAllBySpecification(specification);

        List<LocalDateTime> allLeavesDates = new ArrayList<>();
        leaveRequests.stream().map(leaveRequestMapper::getLeavesDates).forEach(allLeavesDates::addAll);

        List<LeaveHistoryDTO.GraphData> graphData = leaveRequestMapper
                .getLeaveCount(allLeavesDates, period)
                .entrySet()
                .stream()
                .map(leaveRequestMapper::getGraphDataFromMapEntry)
                .collect(Collectors.toList());
        return CollectionModel.of(graphData);
    }
}
