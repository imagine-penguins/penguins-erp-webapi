package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.ParentController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ParentLoginResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ParentModelAssembler implements RepresentationModelAssembler<ParentLoginResponseDTO, EntityModel<ParentLoginResponseDTO>> {
    @Override
    public CollectionModel<EntityModel<ParentLoginResponseDTO>> toCollectionModel(Iterable<? extends ParentLoginResponseDTO> entities) {
        List<EntityModel<ParentLoginResponseDTO>> list = new ArrayList<>();
        entities.forEach(entity -> list.add(toModel(entity)));
        return CollectionModel.of(list,
                linkTo(methodOn(ParentController.class).all()).withSelfRel());
    }

    @Override
    public EntityModel<ParentLoginResponseDTO> toModel(ParentLoginResponseDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ParentController.class).one(entity.getUserId())).withSelfRel(),
                linkTo(methodOn(ParentController.class).all()).withRel("/parents"));
    }
}
