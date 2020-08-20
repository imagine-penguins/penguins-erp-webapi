package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.InstituteDepartmentModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDepartmentDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.InstituteDepartmentFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstituteDepartmentController {
    @Autowired
    InstituteDepartmentFacade facade;

    @Autowired
    InstituteDepartmentModelAssembler assembler;

    @GetMapping("institutes/departments")
    public CollectionModel<EntityModel<InstituteDepartment>> all(){
        return null;
    }

    @GetMapping("institutes/departments/{id}")
    public EntityModel<InstituteDepartmentDTO> one(@PathVariable("id") Long id){
        return assembler.toModel(facade.findById(id));
    }
}
