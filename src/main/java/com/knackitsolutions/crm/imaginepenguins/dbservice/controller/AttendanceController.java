package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.config.DatesConfig;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.AttendanceService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/attendance")
@Slf4j
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceRequestMapper attendanceRequestMapper;
    private final StudentService studentService;
    private final UserService userService;
    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;
    private final AttendanceResponseMapper attendanceResponseMapper;
    private final IAuthenticationFacade authenticationFacade;
    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<String> userAttendance(@RequestBody List<UserAttendanceRequestDTO> dtos) {
        log.debug("Saving Attendance");
        UserContext userContext = (UserContext)authenticationFacade.getAuthentication().getPrincipal();
        User supervisor = userService
                .findById(userContext.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        for (UserAttendanceRequestDTO dto : dtos) {
            Attendance attendance = new Attendance(DatesConfig.now(), dto.getStatus());
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

    private Integer getCount(List<UserAttendanceResponseDTO> users, AttendanceStatus status) {
        return (int) (users
                .stream()
                .filter(user -> user.getStatus().get() == status)
                .count());
    }

    private void addStudentLinks(UserAttendanceResponseDTO student, Optional<Long> classId
            , Optional<Long> userId, Optional<Period> period, Optional<String> value, List<PrivilegeCode> privilegeCodes) {
        if (privilegeCodes.contains(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY)) {
            student.add(linkTo(methodOn(AttendanceController.class)
                    .updateStudentAttendance(student.getAttendanceId().get()
                            , student.getUserId(), null))
                    .withRel(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY.getPrivilegeCode()));
        }
        student.add(linkTo(methodOn(StudentController.class)
                .one(student.getUserId())).withRel("profile"));
        student.add(linkTo(methodOn(AttendanceController.class)
                .userAttendanceHistory(classId, Optional.empty(), userId, period, value)).withRel("view-user-attendance"));
    }

    private void addEmployeeLinks(UserAttendanceResponseDTO employee, Optional<Long> departmentId
            , Optional<Long> userId, Optional<Period> period, Optional<String> value, List<PrivilegeCode> privilegeCodes) {
        if (privilegeCodes.contains(PrivilegeCode.EDIT_EMPLOYEE_ATTENDANCE_HISTORY)){
            employee.add(linkTo(methodOn(AttendanceController.class)
                    .updateAttendance(employee.getAttendanceId().get(), employee.getUserId()
                            , null)).withRel("update-attendance"));
        }
        employee
                .add(linkTo(methodOn(EmployeeController.class).one(employee.getUserId())).withRel("profile"));
        employee
                .add(linkTo(methodOn(AttendanceController.class)
                        .userAttendanceHistory(Optional.empty(), departmentId, userId, period, value))
                        .withRel("view-user-attendance"));
    }

    @GetMapping("/history")
    public AttendanceHistoryDTO userAttendanceHistory(@RequestParam(name = "classId") Optional<Long> classId
            , @RequestParam(name = "departmentId") Optional<Long> departmentId
            , @RequestParam(name = "studentId") Optional<Long> userId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Attendance History for departmentId: {}, classID: {}, period: {}, value: {}, studentId: {}"
                , departmentId, classId, period, value, userId);
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "");
        orders.add(order1);
        orders.add(order2);
        Sort.by(orders);
        Optional<Date> startDate = period
                .map(p -> attendanceRequestMapper.periodStartDateValue(p, value))
                .orElse(Optional.empty());
        Optional<Date> endDate = period
                .map(p -> attendanceRequestMapper.periodEndDateValue(p, value))
                .orElse(Optional.empty());

        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();

        List<PrivilegeCode> privilegeCodes = userService
                .findById(userContext.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()))
                .getUserPrivileges()
                .stream()
                .map(userPrivilege -> userPrivilege.getDepartmentPrivilege().getPrivilege().getPrivilegeCode())
                .collect(Collectors.toList());

        //      Add Filters

        List<StudentAttendance> studentAttendances = classId.map(cid -> userId
                .map(sid -> attendanceService.getStudentAttendancesByStudentId(sid, startDate, endDate))
                .orElseGet(() -> attendanceService.getStudentAttendancesByClassId(cid, startDate, endDate)))
                .orElse(new ArrayList<>());

        studentAttendances.forEach(studentAttendance -> log.debug("Student Attendance: {}", studentAttendance));
        List<UserAttendanceResponseDTO> students = studentAttendances.stream()
                .map(attendanceResponseMapper::mapUserAttendanceToStudent)
                .collect(Collectors.toList());

        List<EmployeeAttendance> employeeAttendances = departmentId.map(did -> userId.map(
                uid -> attendanceService.getEmployeeAttendancesByEmployeeId(uid, startDate, endDate))
                .orElseGet(() -> attendanceService.getEmployeeAttendancesByDepartmentId(did, startDate, endDate))
        ).orElse(new ArrayList<>());


        employeeAttendances.forEach(employeeAttendance -> log.debug("Student Attendance: {}", employeeAttendance));

        List<UserAttendanceResponseDTO> employees = employeeAttendances.stream()
                .map(attendanceResponseMapper::mapUserAttendanceToEmployee)
                .collect(Collectors.toList());

//        students.forEach(student -> addLinks(student, classId, userId, period, value, privilegeCodes));

//        employees.forEach(employee -> addLinks(employee, departmentId, userId, period, value, privilegeCodes));

        log.debug("Preparing graph data.");

        AttendanceHistoryDTO.GraphData graphData = new AttendanceHistoryDTO.GraphData();

        List<UserAttendanceResponseDTO> users = students.isEmpty() ? employees : students;

        graphData.setLeavePercent(getCount(users, AttendanceStatus.LEAVE));
        graphData.setPresentPercent(getCount(users, AttendanceStatus.PRESENT));
        graphData.setAbsentPercent(getCount(users, AttendanceStatus.ABSENT));

        AttendanceHistoryDTO dto = new AttendanceHistoryDTO();

        dto.setGraphData(graphData);
        log.debug("Attendance History Request Completed");
        dto.add(
                linkTo(methodOn(AttendanceController.class)
                        .userAttendanceHistory(classId, departmentId, userId, period, value))
                        .withRel("view-attendance-history")
        );
        return dto;
    }

    private ResponseEntity<String> updateStudentAttendance(Long attendanceId, Long studentId
            , StudentAttendanceUpdateRequestDTO studentAttendanceUpdateRequestDTO) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        StudentAttendanceKey key = new StudentAttendanceKey(studentId, attendanceId);
        StudentAttendance studentAttendance = attendanceService.getStudentAttendanceById(key);

        Attendance attendance = studentAttendance.getAttendance();
        attendance.setSupervisor(
                userService
                        .findById(userContext.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()))
        );
        attendance.setAttendanceStatus(studentAttendanceUpdateRequestDTO.getStatus());
        attendance.setAttendanceDate(studentAttendanceUpdateRequestDTO.getAttendanceDate());
        attendance = attendanceRepository.save(attendance);

        studentAttendance.setAttendance(attendance);

        Optional<StudentAttendance> replacedStudentAttendance = attendanceService.saveAttendance(studentAttendance);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Attendance updated successfully.");
    }

    private ResponseEntity<String> updateEmployeeAttendance(Long attendanceId, Long employeeId
            , UserAttendanceUpdateRequestDTO userAttendanceUpdateRequestDTO) {
        UserContext userContext = (UserContext)authenticationFacade.getAuthentication().getPrincipal();
        EmployeeAttendanceKey key = new EmployeeAttendanceKey(employeeId, attendanceId);

        EmployeeAttendance employeeAttendance = attendanceService.getEmployeeAttendanceById(key);

        Attendance attendance = employeeAttendance.getAttendance();
        attendance.setSupervisor(
                userService
                        .findById(userContext.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()))
        );
        attendance.setAttendanceStatus(userAttendanceUpdateRequestDTO.getStatus());
        attendance.setAttendanceDate(userAttendanceUpdateRequestDTO.getAttendanceDate());
        attendance = attendanceRepository.save(attendance);

        employeeAttendance.setAttendance(attendance);

        Optional<EmployeeAttendance> replacedEmployeeAttendance = attendanceService.saveAttendance(employeeAttendance);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Attendance is updated");
    }

    @PutMapping(value = "/{attendanceId}/users/{userId}")
    public ResponseEntity<String> updateAttendance(@PathVariable("attendanceId") Long attendanceId
            , @PathVariable("userId") Long userId
            , @RequestBody UserAttendanceUpdateRequestDTO userAttendanceUpdateRequestDTO) {
        if (userAttendanceUpdateRequestDTO instanceof StudentAttendanceUpdateRequestDTO) {
            log.info("Updating Student Attendance");
            return updateStudentAttendance(attendanceId, userId, (StudentAttendanceUpdateRequestDTO) userAttendanceUpdateRequestDTO);
        }

        log.info("Updating Employee Attendance");
        return updateEmployeeAttendance(attendanceId, userId, userAttendanceUpdateRequestDTO);
    }

    @GetMapping
    public CollectionModel<?> viewAttendance(@RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, userContext.getUserId());
        User user = userService.findById(userContext.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));

        if (user.getUserType() == UserType.EMPLOYEE) {
            return CollectionModel.of(viewEmployeeAttendance(user.getId(), period, value));
        } else if (user.getUserType() == UserType.STUDENT) {
            return CollectionModel.of(viewStudentAttendance(user.getId(), period, value));
        } else
            return CollectionModel.of(null);
    }

    @GetMapping("/employees/{employeeId}")
    public List<UserAttendanceResponseDTO> viewEmployeeAttendance(@PathVariable("employeeId") Long userId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, userId);

        List<EmployeeAttendance> employeeAttendances = attendanceService.getEmployeeAttendancesByEmployeeId(
                userId
                , period
                        .map(p -> attendanceRequestMapper.periodStartDateValue(p, value))
                        .orElse(Optional.empty())
                , period
                        .map(p -> attendanceRequestMapper.periodEndDateValue(p, value))
                        .orElse(Optional.empty()));

        return employeeAttendances
                .stream()
                .map(attendanceResponseMapper::mapUserAttendanceToEmployee)
                .collect(Collectors.toList());
    }

    //load student for marking attendance
    @GetMapping("/classes/{id}/students")
    public CollectionModel<UserAttendanceResponseDTO> loadClassStudents(@PathVariable("id") Long classSectionId) {
        List<UserAttendanceResponseDTO> responseDTOS = studentService
                .loadStudentWithClassSectionId(classSectionId)
                .stream()
                .map(attendanceResponseMapper::entityToDTO)
                .collect(Collectors.toList());

        responseDTOS.forEach(student -> student.add(linkTo(methodOn(StudentController.class)
                .one(student.getUserId()))
                .withRel("profile")));

        return CollectionModel.of(responseDTOS
                , linkTo(methodOn(StudentController.class).all()).withRel("all-students")
                , linkTo(methodOn(AttendanceController.class).loadClassStudents(classSectionId)).withSelfRel()
                , linkTo(methodOn(AttendanceController.class).userAttendance(null)).withRel("save-attendance"));
    }

    @GetMapping("/users")
    public CollectionModel<UserAttendanceResponseDTO> loadUsers() {
        log.trace("loadUsers started...");
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        User user = userService.findById(userContext.getUserId())
                .orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        List<GrantedAuthority> grantedAuthorities = userContext.getAuthorities();
        grantedAuthorities.stream().forEach(grantedAuthority -> log.debug("GrantedAuthority: {}", grantedAuthority.getAuthority()));
        List<UserAttendanceResponseDTO> userAttendanceResponseDTOS = new ArrayList<>();
        log.debug("GrantedAuthority MARK_EMPLOYEE: {}", PrivilegeCode.MARK_EMPLOYEE_ATTENDANCE.getPrivilegeCode());
        log.debug("Is Mark Employee Access Present: {}", grantedAuthorities.contains(new SimpleGrantedAuthority(
                PrivilegeCode.MARK_EMPLOYEE_ATTENDANCE.getPrivilegeCode()
        )));
        log.debug("Is Mark Student Access Present: {}", grantedAuthorities.contains(new SimpleGrantedAuthority(
                PrivilegeCode.MARK_STUDENT_ATTENDANCE.getPrivilegeCode()
        )));
        if (grantedAuthorities.contains(new SimpleGrantedAuthority(PrivilegeCode.MARK_EMPLOYEE_ATTENDANCE.getPrivilegeCode()))) {
            log.debug("Fetching employee subordinates.");
            Employee employee = (Employee) user;
            employee
                    .getSubordinates()
                    .stream()
                    .map(attendanceResponseMapper::entityToDTO)
                    .forEach(userAttendanceResponseDTOS::add);
            log.debug("fetch employees completed");
        }
        if (grantedAuthorities.contains(new SimpleGrantedAuthority(PrivilegeCode.MARK_STUDENT_ATTENDANCE.getPrivilegeCode()))) {
            log.debug("fetching students of their classes.");
            Teacher teacher = (Teacher) user;
            List<Student> students = new ArrayList<>();
            teacher
                    .getInstituteClassSections()
                    .stream()
                    .map(instituteClassSection -> instituteClassSection.getStudents())
                    .forEach(students::addAll);
            students
                    .stream()
                    .map(attendanceResponseMapper::entityToDTO)
                    .forEach(userAttendanceResponseDTOS::add);
            log.debug("fetch students completed.");
        }
        log.trace("loadUsers Request Completed.");
        return CollectionModel.of(userAttendanceResponseDTOS
                , linkTo(
                        methodOn(AttendanceController.class)
                                .userAttendance(null)
                ).withRel("save-attendance")
        );
    }

    //View Self Attendance
    @GetMapping("/students/{studentId}")
    public List<UserAttendanceResponseDTO> viewStudentAttendance(@PathVariable("studentId") Long userId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, userId);

        List<StudentAttendance> studentAttendances = null;
        studentAttendances = attendanceService.getStudentAttendancesByStudentId(
                userId
                , period
                        .map(p -> attendanceRequestMapper.periodStartDateValue(p, value))
                        .orElse(Optional.empty())
                , period
                        .map(p -> attendanceRequestMapper.periodEndDateValue(p, value))
                        .orElse(Optional.empty()));

        return studentAttendances
                .stream()
                .map(attendanceResponseMapper::mapUserAttendanceToStudent)
                .collect(Collectors.toList());
    }

}
