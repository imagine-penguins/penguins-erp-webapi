package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.PrivilegeModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.PrivilegeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PrivilegeController {

    @Autowired
    PrivilegeFacade privilegeFacade;

    @Autowired
    PrivilegeModelAssembler assembler;

    @PostMapping("/privileges")
    public ResponseEntity<?> newPrivilege(PrivilegeDTO dto){
        EntityModel<PrivilegeDTO> entityModel = assembler.toModel(privilegeFacade.newPrivilege(dto));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    @GetMapping("/privileges/{id}")
    public EntityModel<PrivilegeDTO> one(@PathVariable("id") Integer id){
        return assembler.toModel(privilegeFacade.one(id));
    }
    
    @GetMapping("/privileges")
    public CollectionModel<EntityModel<PrivilegeDTO>> all(){
        return CollectionModel.of(privilegeFacade.allPrivileges()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()),
            linkTo(methodOn(PrivilegeController.class).all()).withRel("/privileges"));
    }
}
