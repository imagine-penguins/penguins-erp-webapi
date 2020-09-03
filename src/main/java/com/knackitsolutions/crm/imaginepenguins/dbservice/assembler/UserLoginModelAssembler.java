package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.DashboardController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.UserControllerImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserLoginModelAssembler implements RepresentationModelAssembler<UserLoginResponseDTO, EntityModel<UserLoginResponseDTO>> {
    @Override
    public EntityModel<UserLoginResponseDTO> toModel(UserLoginResponseDTO dto) {
        return EntityModel.of(dto,
                    linkTo(methodOn(UserControllerImpl.class).one(dto.getUserId())).withSelfRel(),
                    linkTo(methodOn(UserControllerImpl.class).all()).withRel("users"),
                    linkTo(methodOn(DashboardController.class).webDashboardDTO(dto.getUserId(), 1l)).withRel("web-dashboard"),
                    linkTo(methodOn(UserControllerImpl.class).institute(dto.getUserId())).withRel("institute")
                );
    }
}
