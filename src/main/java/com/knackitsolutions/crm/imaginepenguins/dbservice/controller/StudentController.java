package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.StudentModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.StudentFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
