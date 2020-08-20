package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.UserControllerImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserLoginResponseDTO, EntityModel<UserLoginResponseDTO>> {
    @Override
    public EntityModel<UserLoginResponseDTO> toModel(UserLoginResponseDTO user) {
        return EntityModel.of(user,
                    linkTo(methodOn(UserControllerImpl.class).one(user.getId())).withSelfRel(),
                    linkTo(methodOn(UserControllerImpl.class).all()).withRel("/users")
                );
    }
}
