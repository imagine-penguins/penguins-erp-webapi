package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.TeacherModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ClassSectionSubjectMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.TeacherFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@Slf4j
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
    public EntityModel<ClassSectionSubjectDTO> classes(@PathVariable("id") Long teacherId){

        return EntityModel.of(facade.loadClasses(teacherId));

    }


}
