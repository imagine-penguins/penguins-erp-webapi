package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.ParentModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ParentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.ParentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ParentController {

    @Autowired
    ParentFacade parentFacade;

    @Autowired
    ParentModelAssembler assembler;

    @GetMapping("/parents")
    public CollectionModel<EntityModel<ParentLoginResponseDTO>> all(){
        return assembler.toCollectionModel(parentFacade.findAll());
//        CollectionModel.of(parentFacade.findAll().stream().map(assembler::toModel).collect(Collectors.toList()),
//                linkTo(methodOn(ParentController.class).all()).withSelfRel());
    }

    @GetMapping("/parents/{id}")
    public EntityModel<ParentLoginResponseDTO> one(@PathVariable Long id){
        return assembler.toModel(parentFacade.findById(id));
    }

}
