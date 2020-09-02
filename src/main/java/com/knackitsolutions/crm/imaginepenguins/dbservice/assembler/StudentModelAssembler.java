package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;


import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.StudentController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<StudentLoginResponseDTO, EntityModel<StudentLoginResponseDTO>> {
    @Override
    public EntityModel<StudentLoginResponseDTO> toModel(StudentLoginResponseDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StudentController.class).one(entity.getUserId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).all()).withRel("/students"));
    }
}
