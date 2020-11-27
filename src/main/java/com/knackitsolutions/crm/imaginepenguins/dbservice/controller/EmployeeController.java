package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.EmployeeModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.EmployeeFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeModelAssembler;
    private final EmployeeFacade employeeFacade;
    private final AttendanceResponseMapper attendanceResponseMapper;

    @GetMapping("/employees")
    public CollectionModel<EntityModel<EmployeeLoginResponseDTO>> all() {
        List<EntityModel<EmployeeLoginResponseDTO>> employeeList = employeeFacade.allEmployee()
                .stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(employeeList,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel()
        );
    }

    @GetMapping("/employees/{id}")
    public EntityModel<EmployeeLoginResponseDTO> one(@PathVariable("id") Long id) {
        EmployeeLoginResponseDTO employee = employeeFacade.oneEmployee(id);
        return employeeModelAssembler.toModel(employee);
    }

    @PostMapping("/employees")
    public ResponseEntity<?> newEmployee(@RequestBody EmployeeCreationDTO employeeDTO) {
        log.info("Employee DTO: {}", employeeDTO);
        EntityModel<EmployeeLoginResponseDTO> newEmployee = employeeModelAssembler.toModel(employeeFacade.newEmployee(employeeDTO));
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

    //Load employee for marking attendance
    /*@GetMapping("attendance/departments/{departmentId}/employees")
    public CollectionModel<EmployeeAttendanceResponseDTO> loadEmployeesByDepartment(
            @PathVariable("departmentId") Long departmentId) {
        List<EmployeeAttendanceResponseDTO> dtos = employeeService
                .getEmployeeByDepartmentId(departmentId)
                .stream()
                .map(attendanceResponseMapper::entityToDTO)
                .collect(Collectors.toList());

        return CollectionModel.of(dtos
                , linkTo(methodOn(EmployeeController.class).loadEmployeesByDepartment(departmentId)).withSelfRel());

    }*/
}
