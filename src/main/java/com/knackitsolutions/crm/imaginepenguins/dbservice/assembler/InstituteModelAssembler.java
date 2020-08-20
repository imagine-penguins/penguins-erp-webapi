package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.InstituteControllerImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class InstituteModelAssembler implements RepresentationModelAssembler<Institute, EntityModel<Institute>> {

    @Override
    public EntityModel<Institute> toModel(Institute institute) {
        return EntityModel.of(institute,
                 linkTo(methodOn(InstituteControllerImpl.class).one(institute.getId())).withSelfRel(),
                 linkTo(methodOn(InstituteControllerImpl.class).all()).withRel("institutes")
        );
    }

    @Override
    public CollectionModel<EntityModel<Institute>> toCollectionModel(Iterable<? extends Institute> entities) {
        return null;
    }
}
