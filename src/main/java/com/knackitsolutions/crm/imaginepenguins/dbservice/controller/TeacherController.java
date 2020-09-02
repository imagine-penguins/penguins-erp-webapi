package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.TeacherModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.TeacherFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TeacherController {

    @Autowired
    private TeacherFacade facade;

    @Autowired
    private TeacherModelAssembler assembler;

    @GetMapping("/teachers")
    public CollectionModel<TeacherLoginResponseDTO> all(){
        return null;
    }

    @GetMapping("/teachers/{id}")
    public EntityModel<TeacherLoginResponseDTO> one(@PathVariable Long id){
        return assembler.toModel(facade.findById(id));
    }
}
