package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.StudentModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.StudentFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@Slf4j
public class StudentController {
    @Autowired
    StudentFacade facade;

    @Autowired
    StudentModelAssembler assembler;

    @Autowired
    StudentService studentService;

    @Autowired
    AttendanceResponseMapper attendanceResponseMapper;

    @Autowired
    AttendanceRequestMapper attendanceRequestMapper;

    @GetMapping("/students")
    public CollectionModel<EntityModel<StudentLoginResponseDTO>> all(){
        return CollectionModel.of(facade.all().stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList()),
                linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }

    @GetMapping("/students/{id}")
    public EntityModel<StudentLoginResponseDTO> one(@PathVariable("id") Long id){
        return assembler.toModel(facade.getOne(id));
    }

    //load student for marking attendance
    @GetMapping("attendance/classes/{id}/students")
    public CollectionModel<StudentAttendanceResponseDTO> loadClassStudents(@PathVariable("id") Long classSectionId) {
        List<StudentAttendanceResponseDTO> responseDTOS = studentService
                .loadStudentWithClassSectionId(classSectionId)
                .stream()
                .map(attendanceResponseMapper::entityToDTO)
                .collect(Collectors.toList());

        responseDTOS.forEach(student -> student.add(linkTo(methodOn(StudentController.class)
                .one(student.getUserId()))
                .withRel("profile")));

        return CollectionModel.of(responseDTOS
                , linkTo(methodOn(StudentController.class).all()).withRel("all-students")
                , linkTo(methodOn(StudentController.class).loadClassStudents(classSectionId)).withSelfRel()
                , linkTo(methodOn(AttendanceController.class).userAttendance(null, null, null, null)).withRel("save-attendance"));
    }

    //View Self Attendance
    @GetMapping("attendance/students/{studentId}")
    public List<StudentAttendanceResponseDTO> viewAttendance(@PathVariable("studentId") Long userId
            , @RequestParam(name = "period") Optional<Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, userId);

        List<StudentAttendance> studentAttendances = null;
        studentAttendances = studentService.getStudentAttendancesByStudentId(
                userId
                , period
                        .map(p -> attendanceRequestMapper.periodStartDateValue(p, value))
                        .orElse(Optional.empty())
                , period
                        .map(p -> attendanceRequestMapper.periodEndDateValue(p, value))
                        .orElse(Optional.empty()));

        return studentAttendances
                .stream()
                .map(attendanceResponseMapper::mapStudentAttendanceToStudent)
                .collect(Collectors.toList());
    }

}
