package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.AttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/attendance")
@Slf4j
public class AttendanceController {

    @Autowired
    AttendanceRequestMapper mapper;

    @Autowired
    StudentService studentService;

    @PostMapping("/students")
    public ResponseEntity<String> studentAttendance(@RequestBody AttendanceRequestDTO dtoList) {
            log.debug("Saving Student Attendance");
            studentService.saveAttendance(mapper.dtoToEntity(dtoList));
            log.debug("Student Attendance Saved");
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Attendance Saved");
    }

}
