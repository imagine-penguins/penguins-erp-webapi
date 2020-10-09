package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.LeaveRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.EmployeeFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.StudentFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/attendance")
@Slf4j
public class AttendanceController {

    @Autowired
    AttendanceRequestMapper mapper;

    @Autowired
    StudentService studentService;

    @Autowired
    UserService userService;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    StudentFacade studentFacade;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeFacade employeeFacade;

    @Autowired
    LeaveRequestMapper leaveRequestMapper;

    @PostMapping("/students")
    public ResponseEntity<String> studentAttendance(@RequestBody StudentAttendanceRequestDTO dtoList) {
        log.debug("Saving Student Attendance");
        studentService.saveAttendance(mapper.dtoToEntity(dtoList));
        log.debug("Student Attendance Saved");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Attendance Saved");
    }

    @PostMapping("/employee")
    public ResponseEntity<String> employeeAttendance(@RequestBody EmployeeAttendanceRequestDTO dtoList) {
        log.debug("Saving Student Attendance");
        employeeService.saveAttendance(mapper.dtoToEntity(dtoList));
        log.debug("Student Attendance Saved");
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

    private Optional<Date> periodDateValue(Period period, Optional<String> value, Boolean startDate) {
        Date date = null;
        String v = value.orElseThrow(() -> new IllegalArgumentException("value of the period is not found"));
        try {
            if (startDate) {
                date = period.startDate(v);
                log.debug("start date: {}", date);
            }
            else {
                date = period.endDate(v);
                log.debug("end date: {}", date);
            }
        } catch (ParseException parseException) {
            throw new IllegalArgumentException("value of the period is invalid." +
                    " Expected format dd-MM-yyyy ex. 01-01-2020 translates to 1 Jan 2020");
        }
        return Optional.ofNullable(date);
    }

    @GetMapping(value = {""})
    public AttendanceHistoryDTO userAttendanceHistory(@RequestParam(name = "classId") Optional<Long> classId
            , @RequestParam(name = "departmentId") Optional<Long> departmentId
            , @RequestParam(name = "studentId") Optional<Long> userId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Attendance History for departmentId: {}, classID: {}, period: {}, value: {}, studentId: {}", departmentId, classId, period, value, userId);

        List<StudentAttendance> studentAttendances = null;
        List<EmployeeAttendance> employeeAttendances = null;
        studentAttendances = classId.map(cid -> userId
                .map(sid -> studentService.getStudentAttendancesByStudentId(
                        sid
                        , period
                                .map(p -> periodDateValue(p, value, true))
                                .orElse(Optional.empty())
                        , period
                                .map(p -> periodDateValue(p, value, false))
                                .orElse(Optional.empty()))
                )
                .orElse(studentService.getStudentAttendancesByClassId(
                        cid
                        , period
                                .map(p -> periodDateValue(p, value, true))
                                .orElse(Optional.empty())
                        , period
                                .map(p -> periodDateValue(p, value, false))
                                .orElse(Optional.empty())
                ))).orElse(null);

        employeeAttendances = departmentId.map(did -> userId.map(
                uid -> employeeService.getEmployeeAttendancesByEmployeeId(uid
                        , period
                                .map(period1 -> periodDateValue(period1, value, true))
                                .orElse(Optional.empty())
                        , period
                                .map(p -> periodDateValue(p, value, false))
                                .orElse(Optional.empty())))
                .orElse(employeeService.getEmployeeAttendancesByDepartmentId(
                        did
                        , period
                                .map(p -> periodDateValue(p, value, true))
                                .orElse(Optional.empty())
                        , period
                                .map(p -> periodDateValue(p, value, false))
                                .orElse(Optional.empty()))
        )).orElse(null);

        studentAttendances.forEach(studentAttendance -> log.debug("Student Attendance: {}", studentAttendance));
        employeeAttendances.forEach(employeeAttendance -> log.debug("Student Attendance: {}", employeeAttendance));

        log.debug("Preparing students information.");
        List<StudentAttendanceResponseDTO> students = studentAttendances.stream()
                .map(studentFacade::mapStudentAttendanceToStudent)
                .map(student -> {
                    student.add(linkTo(methodOn(StudentController.class)
                            .one(student.getUserId())).withRel("profile"));
                    student.add(linkTo(methodOn(AttendanceController.class)
                            .userAttendanceHistory(classId, departmentId, Optional.ofNullable(student.getUserId())
                                    , period, value)).withRel("view-self-attendance"));
                    return (StudentAttendanceResponseDTO)student.add(linkTo(methodOn(AttendanceController.class)
                            .updateStudentAttendance(student.getAttendanceId().get()
                                    , student.getUserId(), null))
                            .withRel("update-attendance")
                    );
                })
                .collect(Collectors.toList());

        List<UserAttendanceResponseDTO> users = employeeAttendances.stream()
                .map(employeeAttendance -> employeeFacade.mapEmployeeAttendanceToEmployee(employeeAttendance))
                .map(employee -> {
                    employee.add(linkTo(methodOn(AttendanceController.class)
                            .userAttendanceHistory(classId, departmentId, Optional.ofNullable(employee.getUserId())
                                    , period, value)).withRel("self-attendance"));
                    employee.add(linkTo(methodOn(EmployeeController.class).one(employee.getUserId())).withRel("profile"));
                    return (UserAttendanceResponseDTO) employee.add(linkTo(methodOn(AttendanceController.class)
                            .updateAttendance(employee.getAttendanceId().get(), employee.getUserId()
                                    , null)).withRel("update-attendance"));
                })
                .collect(Collectors.toList());

        log.debug("Preparing graph data.");

        AttendanceHistoryDTO.GraphData graphData = new AttendanceHistoryDTO.GraphData();

        if ( students != null && !students.isEmpty())
            users = students.stream().map(student -> (UserAttendanceResponseDTO) student).collect(Collectors.toList());

        graphData.setLeavePercent(getCount(users, AttendanceStatus.LEAVE));
        graphData.setPresentPercent(getCount(users, AttendanceStatus.PRESENT));
        graphData.setAbsentPercent(getCount(users, AttendanceStatus.ABSENT));

        AttendanceHistoryDTO dto = new AttendanceHistoryDTO();
        if ( students != null || !students.isEmpty())
            dto.setStudents(students);
        if (users != null && !users.isEmpty())
            dto.setUsers(users);
        dto.setGraphData(graphData);
        log.debug("Attendance History Request Completed");
        return dto;
    }

    public ResponseEntity<String> updateStudentAttendance(@PathVariable("attendanceId") Long attendanceId
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

    public ResponseEntity<String> updateEmployeeAttendance(@PathVariable("attendanceId") Long attendanceId
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

    @GetMapping("/leave-request")
    public LeaveHistoryDTO leaveRequestHistory(@RequestParam(name = "class") Optional<Long> classId
            , @RequestParam(name = "department") Optional<Long> departmentId
            , @RequestParam(name = "user") Optional<Long> userId) {
        LeaveHistoryDTO historyDTO = new LeaveHistoryDTO();

        List<Student> students = classId
                .map(id -> studentService.loadStudentWithClassSectionId(id)).orElse(null);

        List<User> users = departmentId
                .map(id -> userService.findByDepartmentId(id)).orElse(null);


        if (users == null) {
            if (students != null)
                users = students.stream().map(student -> (User)student).collect(Collectors.toList());
            else
                users = Collections.EMPTY_LIST;
        }

        historyDTO.setLeaveResponseDTO(leaveRequestMapper.leaveResponseDTOFromUser(users));

        return historyDTO;

    }

    public enum Period {

        MONTH("M"){
            @Override
            Date startDate(String value) {
                Month month = Month.of(Integer.parseInt(value));
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDate date = LocalDate.now().withMonth(month.getValue());//(Year.now().getValue(), month, 1);
                LocalDate monthStart = date.withDayOfMonth(1);
                LocalDate monthEnd = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
                return Date.from(monthStart.atStartOfDay(zoneId).toInstant());
            }

            @Override
            Date endDate(String value) {
                Month month = Month.of(Integer.parseInt(value));
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDate date = LocalDate.now().withMonth(month.getValue());//(Year.now().getValue(), month, 1);
                LocalDate monthEnd = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
                return Date.from(monthEnd.atStartOfDay(zoneId).toInstant());
            }
        },
        WEEK("W"){
            @Override
            Date startDate(String value) {
                Integer weekNumber = Integer.valueOf(value);
                LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);
                return Date.from(week.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            @Override
            Date endDate(String value) {
                Integer weekNumber = Integer.valueOf(value);
                LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);
                return Date.from(week.with(DayOfWeek.MONDAY).plusDays(6).atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        },
        DAY("D"){
            @Override
            Date startDate(String value) throws ParseException{
                return new SimpleDateFormat("dd-MM-yyyy").parse(value);
            }

            @Override
            Date endDate(String value) throws ParseException {
                return new SimpleDateFormat("dd-MM-yyyy").parse(value);
            }
        };
        private String period;

        Period(String period) {
            this.period = period;
        }

        public String getPeriod() {
            return period;
        }

        public static Period of(String period) {
            return Stream.of(Period.values())
                    .filter(p -> p.getPeriod().equals(period))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        abstract Date startDate(String value) throws DateTimeException, ParseException;
        abstract Date endDate(String value) throws DateTimeException, ParseException;
    }

}
