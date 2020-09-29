package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.config.DatesConfig;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.AttendanceHistoryDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.AttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceUpdateDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
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
import java.util.ArrayList;
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

    @PostMapping("/students")
    public ResponseEntity<String> studentAttendance(@RequestBody AttendanceRequestDTO dtoList) {
        log.debug("Saving Student Attendance");
        studentService.saveAttendance(mapper.dtoToEntity(dtoList));
        log.debug("Student Attendance Saved");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Attendance Saved");
    }

    private AttendanceHistoryDTO.Student mapStudentAttendanceToStudent(StudentAttendance studentAttendance) {


        AttendanceHistoryDTO.Student student = new AttendanceHistoryDTO.Student();
        student.setStudentAttendanceKey(studentAttendance.getStudentAttendanceKey());
        student.setStatus(studentAttendance.getAttendance().getAttendanceStatus());
        student.setRollNumber(studentAttendance.getStudent().getRollNumber());
        student.setName(
                studentAttendance.getStudent().getUserProfile().getFirstName()
                        + " "
                        + studentAttendance.getStudent().getUserProfile().getFirstName()
        );
        return student;
    }

    private Integer getCount(List<AttendanceHistoryDTO.Student> students, AttendanceStatus status) {
        return (int)(students
                .stream()
                .filter(student -> student.getStatus() == status)
                .count());
    }


    @GetMapping(value = {"/class/{classId}"})
    public AttendanceHistoryDTO attendanceHistory(@PathVariable(name = "classId") Long classId
            , @RequestParam(name = "studentId") Optional<Long> studentId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        List<StudentAttendance> studentAttendances = null;
        if (studentId.isPresent()){
            studentAttendances = studentService.getStudentAttendancesByStudentId(
                    studentId.get(), Optional.ofNullable(period.map(p -> {
                        try {
                            return p.startDate(value.orElseThrow(() -> new IllegalArgumentException("value of the period is invalid")));
                        } catch (ParseException parseException) {
                            throw new IllegalArgumentException("value of the period is invalid");
                        }
                    }).orElse(null)), Optional.ofNullable(period.map(p -> {
                        try {
                            return p.endDate(value.orElseThrow(() -> new IllegalArgumentException("value of the period is invalid")));
                        } catch (ParseException parseException) {
                            throw new IllegalArgumentException("value of the period is invalid");
                        }
                    }).orElse(null)));
        }
        else {
            studentAttendances = studentService.getStudentAttendancesByClassId(classId, Optional.ofNullable(period.map(p -> {
                try {
                    return p.startDate(value.orElseThrow(() -> new IllegalArgumentException("value of the period is invalid")));
                } catch (ParseException parseException) {
                    throw new IllegalArgumentException("value of the period is invalid");
                }
            }).orElse(null)), Optional.ofNullable(period.map(p -> {
                try {
                    return p.endDate(value.orElseThrow(() -> new IllegalArgumentException("value of the period is invalid")));
                } catch (ParseException parseException) {
                    throw new IllegalArgumentException("value of the period is invalid");
                }
            }).orElse(null)));

        }

        List<AttendanceHistoryDTO.Student> students = studentAttendances.stream()
                .map(this::mapStudentAttendanceToStudent)
                .map(student -> {
                    student.add(linkTo(methodOn(StudentController.class)
                            .one(student.getStudentAttendanceKey().getStudentId())).withRel("profile"));
                    return student.add(linkTo(methodOn(AttendanceController.class)
                            .updateAttendance(student.getStudentAttendanceKey().getAttendanceId()
                                    , student.getStudentAttendanceKey().getStudentId(), null))
                            .withRel("update-attendance")
                    );
                })
                .collect(Collectors.toList());

        AttendanceHistoryDTO.GraphData graphData = new AttendanceHistoryDTO.GraphData();
        graphData.setLeavePercent(getCount(students, AttendanceStatus.LEAVE));
        graphData.setPresentPercent(getCount(students, AttendanceStatus.PRESENT));
        graphData.setAbsentPercent(getCount(students, AttendanceStatus.ABSENT));

        AttendanceHistoryDTO dto = new AttendanceHistoryDTO();
        dto.setStudents(students);
        dto.setGraphData(graphData);

        return dto;
    }

    @PutMapping(value = "/{attendanceId}/{studentId}")
    public ResponseEntity<String> updateAttendance(@PathVariable("attendanceId") Long attendanceId
            , @PathVariable("studentId") Long studentId
            , @RequestBody StudentAttendanceUpdateDTO studentAttendanceUpdateDTO) {

        StudentAttendanceKey key = new StudentAttendanceKey(studentId, attendanceId);
        StudentAttendance studentAttendance = studentService.getStudentAttendanceById(key);

        Attendance attendance = studentAttendance.getAttendance();
        attendance.setUpdateTime(DatesConfig.now());
        attendance.setUser(Optional
                .of(userService.findById(studentAttendanceUpdateDTO.getSupervisorId()))
                .orElseThrow(() -> new UserNotFoundException(studentAttendanceUpdateDTO.getSupervisorId())));
        attendance.setAttendanceStatus(studentAttendanceUpdateDTO.getStatus());
        attendance.setAttendanceDate(studentAttendanceUpdateDTO.getAttendanceDate());
        attendance = attendanceRepository.save(attendance);

        studentAttendance.setAttendance(attendance);

        Optional<StudentAttendance> replacedStudentAttendance = studentService.saveAttendance(studentAttendance);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Attendance is updated");
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
                return startDate(value);
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
