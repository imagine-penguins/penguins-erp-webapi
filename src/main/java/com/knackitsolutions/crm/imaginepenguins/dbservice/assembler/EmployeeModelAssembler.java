package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.EmployeeController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeLoginResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<EmployeeLoginResponseDTO, EntityModel<EmployeeLoginResponseDTO>> {

//        @Override
//    public CollectionModel<EntityModel<Employee>> toCollectionModel(Iterable<? extends Employee> entities) {
//        return null;
//    }

    @Override
    public EntityModel<EmployeeLoginResponseDTO> toModel(EmployeeLoginResponseDTO entity) {

        return EntityModel.of(entity,
                linkTo(methodOn(EmployeeController.class).one(entity.getUserId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
        );
    }
}
