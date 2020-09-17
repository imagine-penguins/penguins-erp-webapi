package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.TeacherModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ClassSectionSubjectMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.TeacherFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherFacade facade;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassSectionSubjectMapper classSectionSubjectMapper;

    @Autowired
    private TeacherModelAssembler assembler;

    @GetMapping("")
    public CollectionModel<TeacherLoginResponseDTO> all() {
        return null;
    }

    @GetMapping("/{id}")
    public EntityModel<TeacherLoginResponseDTO> one(@PathVariable("id") Long id) {
        return assembler.toModel(facade.findById(id));
    }

    @GetMapping("/{id}/classes")
    public EntityModel<ClassSectionSubjectDTO> classes(@PathVariable("id") Long id){

        return EntityModel.of(facade.loadClasses(id));

    }

}
