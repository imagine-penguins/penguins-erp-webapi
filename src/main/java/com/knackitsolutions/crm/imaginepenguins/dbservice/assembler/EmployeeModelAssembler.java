package com.knackitsolutions.crm.imaginepenguins.dbservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.EmployeeController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<EmployeeCreationDTO, EntityModel<EmployeeCreationDTO>> {

//        @Override
//    public CollectionModel<EntityModel<Employee>> toCollectionModel(Iterable<? extends Employee> entities) {
//        return null;
//    }

    @Override
    public EntityModel<EmployeeCreationDTO> toModel(EmployeeCreationDTO entity) {

        return EntityModel.of(entity,
                linkTo(methodOn(EmployeeController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
        );
    }
}
