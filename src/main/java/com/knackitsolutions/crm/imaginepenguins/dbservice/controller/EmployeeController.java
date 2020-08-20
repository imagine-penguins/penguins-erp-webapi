package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.EmployeeModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeUpdateDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.EmployeeFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeModelAssembler employeeModelAssembler;

    @Autowired
    EmployeeFacade employeeFacade;

    @GetMapping("/employees")
    public CollectionModel<EntityModel<EmployeeCreationDTO>> all() {
        List<EntityModel<EmployeeCreationDTO>> employeeList = employeeFacade.allEmployee()
                .stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(employeeList,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel()
        );
    }

    @GetMapping("/employees/{id}")
    public EntityModel<EmployeeCreationDTO> one(@PathVariable("id") Long id) {
        EmployeeCreationDTO employee = employeeFacade.oneEmployee(id);
        return employeeModelAssembler.toModel(employee);
    }

    @PostMapping("/employees")
    public ResponseEntity<?> newEmployee(@RequestBody EmployeeCreationDTO employeeDTO) {
        log.info("Employee DTO: {}", employeeDTO);
        EntityModel<EmployeeCreationDTO> newEmployee = employeeModelAssembler.toModel(employeeFacade.newEmployee(employeeDTO));
        return ResponseEntity.created(newEmployee.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(newEmployee);
    }

//    @PutMapping("/employees/{id}")
//    public ResponseEntity<?> replaceEmployee(@PathVariable("id") Long id, @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
//        EmployeeCreationDTO oldEmployee = employeeFacade.oneEmployee(id);
//        oldEmployee.setManager(employee.getManager());
//                    return employeeRepository.save(employee1);
//        EntityModel<EmployeeCreationDTO> model = employeeModelAssembler.toModel(oldEmployee);
//        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
//                .body(model);
//    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> delete(Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
