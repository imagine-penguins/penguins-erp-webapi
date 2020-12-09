package com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.LeaveRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveHistoryDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestUpdateDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
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

    @PostMapping
    public ResponseEntity<String> saveLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        log.trace("Request for new leave application. Processing...");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        User user = userService
                .findById(userContext.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));

        if (leaveRequestDTO.getApprovesId() == null) {
            if (user instanceof Employee) {
                leaveRequestDTO.setApprovesId(((Employee) user).getManager().getId());
            } else if (user instanceof Student) {
                leaveRequestDTO.setApprovesId(((Student) user).getInstituteClassSection().getTeacher().getId());
            }
        }
        LeaveRequest newLeaveRequest = leaveRequestMapper.dtoToEntity(leaveRequestDTO);

        user.setLeaveRequests(newLeaveRequest);

        User approves = userService
                .findById(leaveRequestDTO.getApprovesId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        approves.setLeaveRequestsApprover(newLeaveRequest);

        newLeaveRequest.setLeaveRequestStatus(LeaveRequestStatus.PENDING);
        LeaveRequest savedLeaveRequest = leaveRequestService
                .saveLeaveRequest(newLeaveRequest);

        log.trace("Request for new leave application. Finished...");
        return ResponseEntity.status(HttpStatus.CREATED).body("Leave Request saved.");
    }

    @GetMapping
    public CollectionModel<LeaveResponseDTO> all(
            @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id") String[] sort
            , @RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
            , @RequestParam Optional<Period> period
            , @RequestParam Optional<String> value
    ) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, SortingService.sort(sort));
        final Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);
        Optional<Date> startDate = period
                .map(p -> FilterService.periodStartDateValue(p, value))
                .orElse(Optional.empty());
        Optional<Date> endDate = period
                .map(p -> FilterService.periodEndDateValue(p, value))
                .orElse(Optional.empty());
        List<LeaveResponseDTO> responseDTOS = new ArrayList<>();
        Specification<LeaveRequest> leaveRequestSpecification = Specification.where(null);
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.VIEW_APPLIED_LEAVE_REQUEST.getPrivilegeCode())
        )) {
            log.debug("view received leaves");
            leaveRequestSpecification = leaveRequestSpecification
                    .and(LeaveRequestSpecification.leaveRequestByUserId(userContext.getUserId()));
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
        Page<LeaveRequest> leaveRequestPage = leaveRequestService.findAllBySpecification(leaveRequestSpecification, pageable);
        leaveRequestPage
                .get()
                .map(leaveRequestMapper::entityToDTO)
                .map(leaveResponseDTO -> leaveResponseDTO.add(
                        linkTo(methodOn(LeaveRequestController.class)
                                .updateLeaveRequestStatus(leaveResponseDTO.getId(), null, null))
                                .withRel(PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS.getPrivilegeCode())))
                .forEach(responseDTOS::add);
        return CollectionModel.of(responseDTOS, linkTo(methodOn(LeaveRequestController.class).leaveRequestHistory(
                null, null, 0, 10, null, null
        )).withSelfRel());
    }

    @PutMapping("/{leaveRequestId}")
    public ResponseEntity<String> updateLeaveRequest(@PathVariable("leaveRequestId") Long leaveRequestId
            , @RequestBody LeaveRequestUpdateDTO dto) {
        LeaveRequest oldLeaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave Request Not Found With the provided Id"));
        leaveRequestMapper.dtoToEntity(oldLeaveRequest, dto);
        LeaveRequest newLeaveRequest = leaveRequestRepository.save(oldLeaveRequest);
        if (newLeaveRequest != null)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Leave Updated.");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Leave not Updated.");
    }

    @PutMapping("/{leaveRequestId}/status/{status}")
    public ResponseEntity<String> updateLeaveRequestStatus(@PathVariable("leaveRequestId") Long leaveRequestId
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
    public CollectionModel<LeaveResponseDTO> leaveRequestHistory(
            @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id") String[] sort
            , @RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value
    ) {

        log.trace("leave history started...");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, SortingService.sort(sort));
        final Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);
        Optional<Date> startDate = period
                .map(p -> FilterService.periodStartDateValue(p, value))
                .orElse(Optional.empty());
        Optional<Date> endDate = period
                .map(p -> FilterService.periodEndDateValue(p, value))
                .orElse(Optional.empty());
        List<LeaveResponseDTO> responseDTOS = new ArrayList<>();
        Specification<LeaveRequest> leaveRequestSpecification = Specification.where(null);
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

        Page<LeaveRequest> leaveRequestPage = leaveRequestService.findAllBySpecification(leaveRequestSpecification, pageable);
        leaveRequestPage
                .get()
                .map(leaveRequestMapper::entityToDTO)
                .map(leaveResponseDTO -> leaveResponseDTO.add(
                        linkTo(methodOn(LeaveRequestController.class)
                                .updateLeaveRequestStatus(leaveResponseDTO.getId(), null, null))
                                .withRel(PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS.getPrivilegeCode())))
                .forEach(responseDTOS::add);
        return CollectionModel.of(responseDTOS, linkTo(methodOn(LeaveRequestController.class).leaveRequestHistory(
                null, null, 0, 10, null, null
        )).withSelfRel());
    }

    @GetMapping("/history/graph")
    public CollectionModel<LeaveHistoryDTO.GraphData> graph(@RequestParam(required = false) String[] search) {
        Map<String, Integer> graph = new HashMap<>();
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();

        Specification<LeaveRequest> specification = LeaveRequestSpecification
                .leaveRequestByApprovesId(userContext.getUserId());
        Map<String, List<SearchCriteria>> searchCriteriaMap = FilterService.createSearchMap(search);

        if (searchCriteriaMap.containsKey("user")) {
            //single user id
            Long userId = Long.parseLong(searchCriteriaMap.get("user").get(0).getValue().toString());
            specification = specification.and(LeaveRequestSpecification.leaveRequestByUserId(userId));
        }
        List<LeaveRequest> leaveRequests = leaveRequestService.findAllBySpecification(specification);
        List<Date> allLeavesDates = new ArrayList<>();
        leaveRequests.stream().map(leaveRequestMapper::getLeavesDates).forEach(allLeavesDates::addAll);

        List<LeaveHistoryDTO.GraphData> graphData = leaveRequestMapper
                .getMonthlyLeaveCount(allLeavesDates)
                .entrySet()
                .stream()
                .map(leaveRequestMapper::getGraphDataFromMapEntry)
                .collect(Collectors.toList());

        return CollectionModel.of(graphData);
    }

    @GetMapping("/graph")
    public CollectionModel<LeaveHistoryDTO.GraphData> graph() {
        Map<String, Integer> graph = new HashMap<>();
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Specification<LeaveRequest> specification = LeaveRequestSpecification.leaveRequestByUserId(userContext.getUserId());
        List<LeaveRequest> leaveRequests = leaveRequestService.findAllBySpecification(specification);

        List<Date> allLeavesDates = new ArrayList<>();
        leaveRequests.stream().map(leaveRequestMapper::getLeavesDates).forEach(allLeavesDates::addAll);

        List<LeaveHistoryDTO.GraphData> graphData = leaveRequestMapper
                .getMonthlyLeaveCount(allLeavesDates)
                .entrySet()
                .stream()
                .map(leaveRequestMapper::getGraphDataFromMapEntry)
                .collect(Collectors.toList());
        return CollectionModel.of(graphData);
    }

}
