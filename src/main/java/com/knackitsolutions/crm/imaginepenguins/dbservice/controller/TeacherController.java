package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.TeacherModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ClassSectionSubjectMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.TeacherFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherFacade facade;
    private final TeacherService teacherService;
    private final ClassSectionSubjectMapper classSectionSubjectMapper;
    private final TeacherModelAssembler assembler;
    private final IAuthenticationFacade authenticationFacade;

    @GetMapping("")
    public CollectionModel<TeacherLoginResponseDTO> all() {
        return null;
    }

    @GetMapping("/{id}")
    public EntityModel<TeacherLoginResponseDTO> one(@PathVariable("id") Long id) {
        return assembler.toModel(facade.findById(id));
    }

    @GetMapping("/classes")
    public EntityModel<ClassSectionSubjectDTO> classes(){
        Long teacherId = ((UserContext) authenticationFacade.getAuthentication().getPrincipal()).getUserId();
        return EntityModel.of(facade.loadClasses(teacherId));

    }


}
