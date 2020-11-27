package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.TeacherController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeacherModelAssembler implements RepresentationModelAssembler<TeacherLoginResponseDTO, EntityModel<TeacherLoginResponseDTO>> {
    @Override
    public EntityModel<TeacherLoginResponseDTO> toModel(TeacherLoginResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(TeacherController.class).all()).withRel("/teachers"));
    }
}
