package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.PrivilegeController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PrivilegeModelAssembler implements RepresentationModelAssembler<PrivilegeDTO, EntityModel<PrivilegeDTO>> {
    @Override
    public EntityModel<PrivilegeDTO> toModel(PrivilegeDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PrivilegeController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(PrivilegeController.class).all()).withRel("/privileges"));
    }
}
