package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.InstituteDepartmentController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDepartmentDTO;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InstituteDepartmentModelAssembler implements RepresentationModelAssembler<InstituteDepartmentDTO, EntityModel<InstituteDepartmentDTO>> {
    @Override
    public EntityModel<InstituteDepartmentDTO> toModel(InstituteDepartmentDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(InstituteDepartmentController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(InstituteDepartmentController.class).all()).withRel("/institutes/departments")
        );
    }
}
