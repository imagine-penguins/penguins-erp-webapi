package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.StudentModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.StudentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Controller
public class StudentController {
    @Autowired
    StudentFacade facade;

    @Autowired
    StudentModelAssembler assembler;

    @GetMapping("/students")
    public CollectionModel<EntityModel<StudentLoginResponseDTO>> all(){
        return CollectionModel.of(facade.all().stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList()),
                linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }

    @GetMapping("/students/{id}")
    public EntityModel<StudentLoginResponseDTO> one(Long id){
        return assembler.toModel(facade.getOne(id));
    }

}
