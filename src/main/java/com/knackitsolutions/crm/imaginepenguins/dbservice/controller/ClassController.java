package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.InstituteClassSectionMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteClassSectionDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteClassRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteClassSectionRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassController {
    private final InstituteClassSectionRepository instituteClassSectionRepository;
    private final InstituteClassSectionMapper instituteClassSectionMapper;
    private final IAuthenticationFacade authenticationFacade;
    private final EmployeeService employeeService;

    @GetMapping("/{classSectionId}")
    public EntityModel<InstituteClassSectionDTO> one(@PathVariable Long classSectionId) {
//        Map<String, Object> objectMap = new HashMap<>();
        InstituteClassSection instituteClassSection = instituteClassSectionRepository
                .findById(classSectionId).orElseThrow(() -> new RuntimeException("Class Section Not Found with ID: " + classSectionId));
        InstituteClassSectionDTO instituteClassSectionDTO = instituteClassSectionMapper.toDTO(instituteClassSection);
        return EntityModel.of(instituteClassSectionDTO, linkTo(methodOn(getClass()).all()).withRel("classes"));
    }

    @GetMapping
    public CollectionModel<EntityModel<InstituteClassSectionDTO>> all() {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();

        Institute institute = employeeService.findInstituteById(userContext.getUserId());
        List<EntityModel<InstituteClassSectionDTO>> collect = institute
                .getClasses()
                .stream()
                .flatMap(instituteClass -> instituteClass.getClassSections().stream())
                .map(instituteClassSectionMapper::toDTO)
                .map(instituteClassSectionDTO -> EntityModel.of(instituteClassSectionDTO, linkTo(methodOn(getClass()).one(instituteClassSectionDTO.getInstituteClassSectionId())).withRel("class-section")))
                .collect(Collectors.toList());
        return CollectionModel.of(collect, linkTo(methodOn(getClass()).all()).withSelfRel());
    }

}
