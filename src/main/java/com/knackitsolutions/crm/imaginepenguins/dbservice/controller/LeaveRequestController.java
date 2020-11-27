package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
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
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.LeaveRequestService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    private final StudentService studentService;
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
                leaveRequestDTO.setApprovesId(((Student)user).getInstituteClassSection().getTeacher().getId());
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

    @GetMapping("/history")
    public LeaveHistoryDTO leaveRequestHistory() {
        LeaveHistoryDTO dto = new LeaveHistoryDTO();
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        List<LeaveResponseDTO> response = leaveRequestRepository.findByUserId(userContext.getUserId())
                .stream()
                .map(leaveRequestMapper::entityToDTO)
                .collect(Collectors.toList());
        dto.setLeaveResponseDTO(response);
        List<LeaveHistoryDTO.GraphData> graphData = leaveRequestMapper
                .getMonthlyLeaveCount(leaveRequestMapper.getUserLeavesDates(
                        userService
                                .findById(userContext.getUserId())
                                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()))
                    )
                )
                .entrySet().stream().map(leaveRequestMapper::getGraphDataFromMapEntry).collect(Collectors.toList());

        dto.setGraphData(graphData);
        return dto;
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

    @GetMapping
    public LeaveHistoryDTO leaveRequestHistory(@RequestParam(name = "class") Optional<Long> classId
            , @RequestParam(name = "department") Optional<Long> departmentId
            , @RequestParam(name = "user") Optional<Long> userId) {

        log.info("Leave Request History");
        log.info("class: {}, department: {}, user: {}", classId, departmentId, userId);
        LeaveHistoryDTO historyDTO = new LeaveHistoryDTO();

        List<Student> students = classId
                .map(id -> studentService.loadStudentWithClassSectionId(id)).orElse(new ArrayList<>());

        List<User> users = departmentId
                .map(id -> userService.findByDepartmentId(id)).orElse(new ArrayList<>());

        User user = userId
                .map(aLong -> userService
                        .findById(aLong)
                        .orElseThrow(() -> new UserNotFoundException(aLong))
                ).orElse(null);

        log.info("UserDTO: {}", user);
        if (!students.isEmpty()) {
            users = students.stream().map(student -> (User) student).collect(Collectors.toList());
        }
        userId
                .flatMap(userService::findById)
                .ifPresent(users::add);
        List<LeaveResponseDTO> responseDTOS = leaveRequestMapper
                .leaveResponseDTOFromUser(users);
        responseDTOS.forEach(leaveResponseDTO -> leaveResponseDTO.add(
                linkTo(methodOn(LeaveRequestController.class)
                        .updateLeaveRequestStatus(leaveResponseDTO.getId(), null, null))
                        .withRel("update-leave-request-status")));
        historyDTO.setLeaveResponseDTO(responseDTOS);

        List<Date> allLeavesDates = new ArrayList<>();
        users
                .stream()
                .map(leaveRequestMapper::getUserLeavesDates)
                .forEach(allLeavesDates::addAll);

        List<LeaveHistoryDTO.GraphData> graphData = leaveRequestMapper
                .getMonthlyLeaveCount(allLeavesDates)
                .entrySet()
                .stream()
                .map(leaveRequestMapper::getGraphDataFromMapEntry)
                .collect(Collectors.toList());
        historyDTO.setGraphData(graphData);
        return historyDTO;
    }
}
