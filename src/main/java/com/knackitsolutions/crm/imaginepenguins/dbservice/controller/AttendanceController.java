package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.LeaveRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.EmployeeFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.StudentFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/attendance")
@Slf4j
public class AttendanceController {

    @Autowired
    AttendanceRequestMapper attendanceRequestMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    UserService userService;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    LeaveRequestMapper leaveRequestMapper;

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    @Autowired
    AttendanceResponseMapper attendanceResponseMapper;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<String> userAttendance(@RequestBody AttendanceRequestDTO dto
            , @RequestParam("class") Optional<Long> classId, @RequestParam("department") Optional<Long> departmentId
            , @RequestParam("subject") Optional<Long> subjectId) {
        log.debug("Saving Attendance");
        if ( !(classId.isPresent() || subjectId.isPresent() || departmentId.isPresent()) ) {
            throw new RuntimeException("One of the class or subject or department must be present in the optional parameter of the request.");
        }
        else if( classId.isPresent() && subjectId.isPresent() && departmentId.isPresent() ){
            throw new RuntimeException("Only one of the class or subject or department should be present in the optional parameter of the request.");
        }

        if (classId.isPresent() || subjectId.isPresent()) {
            log.debug("Saving Student Attendance");
            studentService.saveAttendance(attendanceRequestMapper.dtoToEntity(dto, classId, subjectId));
        } else if (departmentId.isPresent()) {
            log.debug("Saving employee Attendance");
            employeeService.saveAttendance(attendanceRequestMapper.dtoToEntity(dto, departmentId));
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

    private void addLinks(StudentAttendanceResponseDTO student, Optional<Long> classId
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

    private void addLinks(EmployeeAttendanceResponseDTO employee, Optional<Long> departmentId
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

    @GetMapping
    public AttendanceHistoryDTO userAttendanceHistory(@RequestParam(name = "classId") Optional<Long> classId
            , @RequestParam(name = "departmentId") Optional<Long> departmentId
            , @RequestParam(name = "studentId") Optional<Long> userId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Attendance History for departmentId: {}, classID: {}, period: {}, value: {}, studentId: {}"
                , departmentId, classId, period, value, userId);

        if ( !(classId.isPresent() || userId.isPresent() || departmentId.isPresent()) )
            throw new RuntimeException("Cannot classId or userId or departmentId one must be present");

        Optional<Date> startDate = period
                .map(p -> attendanceRequestMapper.periodStartDateValue(p, value))
                .orElse(Optional.empty());
        Optional<Date> endDate = period
                .map(p -> attendanceRequestMapper.periodEndDateValue(p, value))
                .orElse(Optional.empty());

        User user = (User) authenticationFacade.getAuthentication().getPrincipal();

        List<PrivilegeCode> privilegeCodes = user.getUserPrivileges()
                .stream()
                .map(userPrivilege -> userPrivilege.getDepartmentPrivilege().getPrivilege().getPrivilegeCode())
                .collect(Collectors.toList());

        List<StudentAttendanceResponseDTO> students = attendanceResponseMapper
                .getStudentAttendanceResponseDTOList(classId, userId, startDate, endDate);
        List<EmployeeAttendanceResponseDTO> employees = attendanceResponseMapper
                .getEmployeeDTOList(classId, userId, startDate, endDate);

        students.forEach(student -> addLinks(student, classId, userId, period, value, privilegeCodes));

        employees.forEach(employee -> addLinks(employee, departmentId, userId, period, value, privilegeCodes));

        log.debug("Preparing graph data.");

        AttendanceHistoryDTO.GraphData graphData = new AttendanceHistoryDTO.GraphData();

        //Stupid type check bypassed
        List<UserAttendanceResponseDTO> users = students.isEmpty() ? (List<UserAttendanceResponseDTO>) (List<?>) employees
                : (List<UserAttendanceResponseDTO>) (List<?>) students;

        graphData.setLeavePercent(getCount(users, AttendanceStatus.LEAVE));
        graphData.setPresentPercent(getCount(users, AttendanceStatus.PRESENT));
        graphData.setAbsentPercent(getCount(users, AttendanceStatus.ABSENT));

        AttendanceHistoryDTO dto = new AttendanceHistoryDTO();
        if (students.isEmpty()) {
            dto.setEmployees(employees);
        } else {
            dto.setStudents(students);
        }

        dto.setGraphData(graphData);
        log.debug("Attendance History Request Completed");
        dto.add(
                linkTo(methodOn(AttendanceController.class)
                        .userAttendanceHistory(classId, departmentId, userId, period, value))
                        .withRel("view-attendance-history")
        );
        return dto;
    }

    private ResponseEntity<String> updateStudentAttendance(@PathVariable("attendanceId") Long attendanceId
            , @PathVariable("studentId") Long studentId
            , @RequestBody StudentAttendanceUpdateRequestDTO studentAttendanceUpdateRequestDTO) {

        StudentAttendanceKey key = new StudentAttendanceKey(studentId, attendanceId);
        StudentAttendance studentAttendance = studentService.getStudentAttendanceById(key);

        Attendance attendance = studentAttendance.getAttendance();
        attendance.setSupervisor(Optional
                .of(userService.findById(studentAttendanceUpdateRequestDTO.getSupervisorId()))
                .orElseThrow(() -> new UserNotFoundException(studentAttendanceUpdateRequestDTO.getSupervisorId())));
        attendance.setAttendanceStatus(studentAttendanceUpdateRequestDTO.getStatus());
        attendance.setAttendanceDate(studentAttendanceUpdateRequestDTO.getAttendanceDate());
        attendance = attendanceRepository.save(attendance);

        studentAttendance.setAttendance(attendance);

        Optional<StudentAttendance> replacedStudentAttendance = studentService.saveAttendance(studentAttendance);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Attendance is updated");
    }

    private ResponseEntity<String> updateEmployeeAttendance(@PathVariable("attendanceId") Long attendanceId
            , @PathVariable("employeeId") Long employeeId
            , @RequestBody UserAttendanceUpdateRequestDTO userAttendanceUpdateRequestDTO) {

        EmployeeAttendanceKey key = new EmployeeAttendanceKey(employeeId, attendanceId);

        EmployeeAttendance employeeAttendance = employeeService.getEmployeeAttendanceById(key);

        Attendance attendance = employeeAttendance.getAttendance();
        attendance.setSupervisor(Optional
                .of(userService.findById(userAttendanceUpdateRequestDTO.getSupervisorId()))
                .orElseThrow(() -> new UserNotFoundException(userAttendanceUpdateRequestDTO.getSupervisorId())));
        attendance.setAttendanceStatus(userAttendanceUpdateRequestDTO.getStatus());
        attendance.setAttendanceDate(userAttendanceUpdateRequestDTO.getAttendanceDate());
        attendance = attendanceRepository.save(attendance);

        employeeAttendance.setAttendance(attendance);

        Optional<EmployeeAttendance> replacedEmployeeAttendance = employeeService.saveAttendance(employeeAttendance);

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

    @PutMapping("/leave-request/{leaveRequestId}/status/{status}")
    public ResponseEntity<String> updateLeaveRequestStatus(@PathVariable("leaveRequestId") Long leaveRequestId
            , @PathVariable("status") LeaveRequestStatus status
            , @RequestParam("reason") Optional<String> reason) {
        LeaveRequest oldLeaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave Request Not Found With the provided Id"));
        if (status == LeaveRequestStatus.REJECTED) {
            oldLeaveRequest
                    .setRejectedReason(
                            reason.orElseThrow(() -> new RuntimeException("For rejected request reason is required.")));
        }
        oldLeaveRequest.setLeaveRequestStatus(status);

        leaveRequestRepository.save(oldLeaveRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Status Updated.");

    }

    @GetMapping("/leave-request")
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

        User user = userId.map(userService::findById).orElse(null);

        log.info("UserDTO: {}", user);
        if (!students.isEmpty()) {
            users = students.stream().map(student -> (User) student).collect(Collectors.toList());
        }
        userId.map(userService::findById).ifPresent(users::add);
        List<LeaveResponseDTO> responseDTOS = leaveRequestMapper
                .leaveResponseDTOFromUser(users);
        responseDTOS.forEach(leaveResponseDTO -> leaveResponseDTO.add(
                linkTo(methodOn(AttendanceController.class)
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
