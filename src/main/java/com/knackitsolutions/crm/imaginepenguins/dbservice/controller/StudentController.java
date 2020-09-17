package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.StudentModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.StudentFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
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
    public CollectionModel<EntityModel<StudentInfo>> loadClassStudents(@PathVariable("id") Long classSectionId) {
        List<EntityModel<StudentInfo>> studentInfoList = studentService
                .loadClassStudents(classSectionId)
                .stream()
                .map(studentInfo -> EntityModel.of(studentInfo, linkTo(methodOn(StudentController.class).one(studentInfo.getId())).withRel("profile")))
                .collect(Collectors.toList());
        return CollectionModel.of(studentInfoList
                , linkTo(methodOn(StudentController.class).all()).withRel("all-students")
                , linkTo(methodOn(StudentController.class).loadClassStudents(classSectionId)).withSelfRel()
                , linkTo(methodOn(AttendanceController.class).studentAttendance(null)).withRel("save-attendance"));
    }

}
