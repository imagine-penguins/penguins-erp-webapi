package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<String> employeeAttendance(@RequestBody StudentAttendanceRequestDTO dtoList) {
        log.debug("Saving Student Attendance");
        studentService.saveAttendance(mapper.dtoToEntity(dtoList));
        log.debug("Student Attendance Saved");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Attendance Saved");
    }

    private Integer getCount(List<StudentAttendanceResponseDTO> students, AttendanceStatus status) {
        return (int) (students
                .stream()
                .filter(student -> student.getStatus().get() == status)
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

    @GetMapping(value = {"/class/{classId}"})
    public AttendanceHistoryDTO attendanceHistory(@PathVariable(name = "classId") Long classId
            , @RequestParam(name = "studentId") Optional<Long> studentId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Attendance History for classID: {}, period: {}, value: {}, studentId: {}", classId, period, value, studentId);
        List<StudentAttendance> studentAttendances = null;
        studentAttendances = studentId
                .map(id -> studentService.getStudentAttendancesByStudentId(
                            id
                            , period
                                    .map(p -> periodDateValue(p, value, true))
                                    .orElse(Optional.empty())
                            , period
                                    .map(p -> periodDateValue(p, value, false))
                                    .orElse(Optional.empty()))
                )
                .orElse(studentService.getStudentAttendancesByClassId(
                        classId
                        , period
                                .map(p -> periodDateValue(p, value, true))
                                .orElse(Optional.empty())
                        , period
                                .map(p -> periodDateValue(p, value, false))
                                .orElse(Optional.empty())
                ));

        studentAttendances.forEach(studentAttendance -> log.debug("Student Attendance: {}", studentAttendance));

        log.debug("Preparing students information.");
        List<StudentAttendanceResponseDTO> students = studentAttendances.stream()
                .map(studentFacade::mapStudentAttendanceToStudent)
                .map(student -> {
                    student.add(linkTo(methodOn(StudentController.class)
                            .one(student.getUserId())).withRel("profile"));
                    return (StudentAttendanceResponseDTO)student.add(linkTo(methodOn(AttendanceController.class)
                            .updateStudentAttendance(student.getAttendanceId().get()
                                    , student.getUserId(), null))
                            .withRel("update-attendance")
                    );
                })
                .collect(Collectors.toList());

        log.debug("Preparing graph data.");
        AttendanceHistoryDTO.GraphData graphData = new AttendanceHistoryDTO.GraphData();
        graphData.setLeavePercent(getCount(students, AttendanceStatus.LEAVE));
        graphData.setPresentPercent(getCount(students, AttendanceStatus.PRESENT));
        graphData.setAbsentPercent(getCount(students, AttendanceStatus.ABSENT));

        AttendanceHistoryDTO dto = new AttendanceHistoryDTO();
        dto.setStudents(students);
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

    @PutMapping(value = "/{attendanceId}/{userId}")
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
