package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.StudentModelAssembler;
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

    @GetMapping("/classes/{id}/students")
    public CollectionModel<StudentAttendanceResponseDTO> loadClassStudents(@PathVariable("id") Long classSectionId) {
        List<StudentAttendanceResponseDTO> studentInfoList = studentService
                .loadStudentResponseDTOWithClassSectionId(classSectionId)
                .stream()
                .map(studentInfo -> (StudentAttendanceResponseDTO)studentInfo
                        .add(linkTo(methodOn(StudentController.class).one(studentInfo.getUserId()))
                                        .withRel("profile")))
                .collect(Collectors.toList());
        return CollectionModel.of(studentInfoList
                , linkTo(methodOn(StudentController.class).all()).withRel("all-students")
                , linkTo(methodOn(StudentController.class).loadClassStudents(classSectionId)).withSelfRel()
                , linkTo(methodOn(AttendanceController.class).studentAttendance(null)).withRel("save-attendance"));
    }

    @GetMapping("/students/{studentId}")
    public List<StudentAttendanceResponseDTO> viewAttendance(@PathVariable("studentId") Long userId
            , @RequestParam(name = "period") Optional<AttendanceController.Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, userId);

        List<StudentAttendance> studentAttendances = null;
        studentAttendances = studentService.getStudentAttendancesByStudentId(
                userId
                , period
                        .map(p -> periodDateValue(p, value, true))
                        .orElse(Optional.empty())
                , period
                        .map(p -> periodDateValue(p, value, false))
                        .orElse(Optional.empty()));

        return studentAttendances
                .stream()
                .map(facade::mapStudentAttendanceToStudent)
                .collect(Collectors.toList());
    }

    private Optional<Date> periodDateValue(AttendanceController.Period period, Optional<String> value, Boolean startDate) {
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

}
