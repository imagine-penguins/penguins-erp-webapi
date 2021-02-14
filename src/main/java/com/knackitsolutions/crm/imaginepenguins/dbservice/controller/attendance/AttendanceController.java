package com.knackitsolutions.crm.imaginepenguins.dbservice.controller.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.StudentController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/attendance")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AttendanceController {

    private final AttendanceRequestMapper attendanceRequestMapper;
    private final StudentService studentService;
    private final UserService userService;
    private final AttendanceRepository attendanceRepository;
    private final UserDocumentStoreRepository userDocumentStoreRepository;
    private final AttendanceResponseMapper attendanceResponseMapper;
    private final IAuthenticationFacade authenticationFacade;
    private final AttendanceService attendanceService;
    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<String> userAttendance(@RequestBody List<UserAttendanceRequestDTO> dtos) {
        log.debug("Saving Attendance");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        User supervisor = userService
                .findById(userContext.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        for (UserAttendanceRequestDTO dto : dtos) {
            Attendance attendance = new Attendance(LocalDateTime.now(), dto.getStatus());
            supervisor.setAttendances(attendance);
            attendance = attendanceService.saveAttendance(attendance);
            User attendant = userService
                    .findById(dto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
            if (attendant instanceof Student) {
                Student studentAttendant = (Student) attendant;
                attendanceService.saveAttendance(attendanceRequestMapper.dtoToEntity(attendance, studentAttendant));
            } else if (attendant instanceof Employee) {
                Employee employeeAttendant = (Employee) attendant;
                attendanceService.saveAttendance(attendanceRequestMapper.dtoToEntity(attendance, employeeAttendant));
            }
        }
        log.debug("Attendance Saved.");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Attendance Saved");

    }

    private ResponseEntity<String> updateStudentAttendance(Long attendanceId, Long studentId
            , AttendanceStatus attendanceStatus) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        StudentAttendanceKey key = new StudentAttendanceKey(studentId, attendanceId);
        StudentAttendance studentAttendance = attendanceService.getStudentAttendanceById(key);

        Attendance attendance = studentAttendance.getAttendance();
        attendance.setSupervisor(
                userService
                        .findById(userContext.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()))
        );
        attendance.setAttendanceStatus(attendanceStatus);
        attendance.setUpdateTime(LocalDateTime.now());
        attendance = attendanceRepository.save(attendance);

        studentAttendance.setAttendance(attendance);

        Optional<StudentAttendance> replacedStudentAttendance = attendanceService.saveAttendance(studentAttendance);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Attendance updated successfully.");
    }

    private ResponseEntity<String> updateEmployeeAttendance(Long attendanceId, Long employeeId
            , AttendanceStatus attendanceStatus) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        EmployeeAttendanceKey key = new EmployeeAttendanceKey(employeeId, attendanceId);

        EmployeeAttendance employeeAttendance = attendanceService.getEmployeeAttendanceById(key);

        Attendance attendance = employeeAttendance.getAttendance();
        attendance.setSupervisor(
                userService
                        .findById(userContext.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()))
        );
        attendance.setAttendanceStatus(attendanceStatus);
        attendance.setUpdateTime(LocalDateTime.now());
        attendance = attendanceRepository.save(attendance);

        employeeAttendance.setAttendance(attendance);

        Optional<EmployeeAttendance> replacedEmployeeAttendance = attendanceService.saveAttendance(employeeAttendance);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Attendance is updated");
    }

    @PutMapping(value = "/{attendanceId}/users/{userId}/status/{status}")
    public ResponseEntity<String> updateAttendance(@PathVariable("attendanceId") Long attendanceId
            , @PathVariable("userId") Long userId
            , @PathVariable("status") AttendanceStatus attendanceStatus) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (user.getUserType() == UserType.STUDENT) {
            log.info("Updating Student Attendance");
            return updateStudentAttendance(attendanceId, userId, attendanceStatus);
        }

        log.info("Updating Employee Attendance");
        return updateEmployeeAttendance(attendanceId, userId, attendanceStatus);
    }

    //View Self Attendance
    @GetMapping
    public CollectionModel<?> selfAttendance(@RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, userContext.getUserId());
//        User user = userService.findById(userContext.getUserId())
//                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        Optional<LocalDateTime> startDate = period
                .map(p -> FilterService.periodStartDateValue(p, value))
                .orElse(Optional.empty());
        Optional<LocalDateTime> endDate = period
                .map(p -> FilterService.periodEndDateValue(p, value))
                .orElse(Optional.empty());
        List<UserAttendanceResponseDTO> attendanceResponseDTOS = new ArrayList<>();
        if (userContext.getUserType() == UserType.EMPLOYEE) {
            attendanceService
                    .getEmployeeAttendancesByEmployeeId(userContext.getUserId(), startDate, endDate)
                    .stream()
                    .map(attendanceResponseMapper::mapUserAttendanceToEmployee)
                    .forEach(attendanceResponseDTOS::add);
        } else if (userContext.getUserType() == UserType.STUDENT) {
            attendanceService
                    .getStudentAttendancesByStudentId(userContext.getUserId(), startDate, endDate)
                    .stream()
                    .map(attendanceResponseMapper::mapUserAttendanceToStudent)
                    .forEach(attendanceResponseDTOS::add);
        }
        return CollectionModel.of(attendanceResponseDTOS
                , linkTo(methodOn(AttendanceController.class).selfAttendance(period, value)).withSelfRel());
    }

    //load student for marking attendance
    @GetMapping("/classes/{id}/students")
    public CollectionModel<UserAttendanceResponseDTO> loadClassStudents(@PathVariable("id") Long classSectionId) {
        List<UserAttendanceResponseDTO> responseDTOS = studentService
                .loadStudentWithClassSectionId(classSectionId)
                .stream()
                .map(attendanceResponseMapper::entityToDTO)
                .collect(Collectors.toList());

        responseDTOS.forEach(student -> student.add(WebMvcLinkBuilder.linkTo(methodOn(StudentController.class)
                .one(student.getUserId()))
                .withRel("profile")));

        return CollectionModel.of(responseDTOS
                , linkTo(methodOn(StudentController.class).all()).withRel("all-students")
                , linkTo(methodOn(AttendanceController.class).loadClassStudents(classSectionId)).withSelfRel()
                , linkTo(methodOn(AttendanceController.class).userAttendance(null)).withRel("save-attendance"));
    }

    //What is the exception and its detail where is occurred? //TODO
    @GetMapping("/users")
    public PagedModel<UserAttendanceResponseDTO> loadUsers(
            @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id,desc") String[] sort
            , @RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        log.trace("/users >> loadUsers started...");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, SortingService.sort(sort));

        final Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);
        List<UserAttendanceResponseDTO> userAttendanceResponseDTOS = new ArrayList<>();

        Specification<User> userSpecification = null;
        try {
            userSpecification = UserSpecification.filterUsers(searchMap);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse date from search parameters");
        }

        Specification<User> specBasedOnPrivilege = Specification.where(null);
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.MARK_STUDENT_ATTENDANCE.getPrivilegeCode())
        )) {
            log.debug("mark student privilege - marking attendance of all students in school");
            specBasedOnPrivilege = specBasedOnPrivilege.or(UserSpecification.studentsByInstituteId(userContext.getInstituteId()));
        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.MARK_CLASS_STUDENT_ATTENDANCE.getPrivilegeCode())
        )) {
            log.debug("mark only their class students");
            User user = userService
                    .findById(userContext.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
            Set<InstituteClassSection> instituteClassSections = ((Teacher) user).getInstituteClassSections();
            List<Long> instituteClassSectionIds = instituteClassSections
                    .stream().map(i -> i.getId()).collect(Collectors.toList());
            specBasedOnPrivilege = specBasedOnPrivilege.or(UserSpecification.studentByClassIn(instituteClassSectionIds));
        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.MARK_EMPLOYEE_ATTENDANCE.getPrivilegeCode())
        )) {
            log.debug("marking all employees present in the institute");
            specBasedOnPrivilege = specBasedOnPrivilege.or(
                    UserSpecification.employeesByInstituteId(userContext.getInstituteId())
            );

        }
        if (userContext.getAuthorities().contains(
                new SimpleGrantedAuthority(PrivilegeCode.MARK_SUBORDINATES_EMPLOYEE_ATTENDANCE.getPrivilegeCode())
        )) {
            log.debug("marking attendance of his subordinates");
            specBasedOnPrivilege = specBasedOnPrivilege.or(
                    UserSpecification.employeeByManagerId(Arrays.asList(userContext.getUserId()))
            );
        }
        userSpecification = userSpecification.and(specBasedOnPrivilege);
        //Not Himself
        userSpecification = userSpecification
                .and(new GenericSpecification(new SearchCriteria("id", userContext.getUserId(), SearchOperation.NOT_EQUAL)));
        log.debug("Calling database to fetch attendance history");
        Page<User> users = userService.findAll(userSpecification, pageable);
        log.debug("DB call completed.");
        users
                .map(user -> {
                    if (user.getUserType() == UserType.STUDENT)
                        return attendanceResponseMapper.mapUserAttendanceToStudent((Student) user);
                    else if (user.getUserType() == UserType.EMPLOYEE) {
                        return attendanceResponseMapper.mapUserAttendanceToEmployee((Employee) user);
                    }
                    return null;
                })
                .map(dto -> {
                    UserDocumentStore docStore = userDocumentStoreRepository
                            .findByUserIdAndDocumentType(dto.getUserId(), UserDocumentType.DISPLAY_PICTURE);
                    if (docStore != null)
                        dto.setProfilePic(docStore.getStoreURL());
                    return dto;
                })
                .map(dto -> {
                    LeaveRequest leaveRequest = leaveRequestService
                            .findByUserIdAndDate(dto.getUserId(), LocalDateTime.now());
                    log.debug("Leave Request: {}", leaveRequest);
                    if (leaveRequest == null || leaveRequest.getLeaveRequestStatus() != LeaveRequestStatus.APPROVED) {
                        dto.setStatus(Optional.of(AttendanceStatus.PRESENT));
                    }else {
                        dto.setLeaveRequestId(leaveRequest.getId());
                        dto.setStatus(Optional.of(AttendanceStatus.LEAVE));
                    }
                    return dto;
                })
                .forEach(userAttendanceResponseDTOS::add);
        log.trace("loadUsers Request Completed.");
        int totalPages = users.getTotalPages();

        return PagedModel.of(userAttendanceResponseDTOS
                , new PagedModel.PageMetadata(size, page, userAttendanceResponseDTOS.size(), totalPages)
                , linkTo(
                        methodOn(AttendanceController.class)
                                .userAttendance(null)
                ).withRel("save-attendance")
        );
    }
}
