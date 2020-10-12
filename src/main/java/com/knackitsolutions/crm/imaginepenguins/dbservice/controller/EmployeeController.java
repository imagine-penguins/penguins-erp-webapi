package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.EmployeeModelAssembler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.EmployeeAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.UserAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.EmployeeFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeAttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
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


    @Autowired
    EmployeeAttendanceRepository employeeAttendanceRepository;

    @Autowired
    AttendanceResponseMapper attendanceResponseMapper;

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

    @GetMapping("attendance/employees/{employeeId}")
    public List<EmployeeAttendanceResponseDTO> viewAttendance(@PathVariable("employeeId") Long userId
            , @RequestParam(name = "period") Optional<AttendanceController.Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, userId);

        List<EmployeeAttendance> employeeAttendances = employeeService.getEmployeeAttendancesByEmployeeId(
                userId
                , period
                        .map(p -> periodDateValue(p, value, true))
                        .orElse(Optional.empty())
                , period
                        .map(p -> periodDateValue(p, value, false))
                        .orElse(Optional.empty()));

        return employeeAttendances
                .stream()
                .map(attendanceResponseMapper::mapEmployeeAttendanceToEmployee)
                .collect(Collectors.toList());
    }

    private Optional<Date> periodDateValue(AttendanceController.Period period, Optional<String> value, Boolean startDate) {
        Date date = null;
        String v = value.orElseThrow(() -> new IllegalArgumentException("value of the period is not found"));
        try {
            if (startDate) {
                date = period.startDate(v);
                log.debug("start date: {}", date);
            }
            else {
                date = period.endDate(v);
                log.debug("end date: {}", date);
            }
        } catch (ParseException parseException) {
            throw new IllegalArgumentException("value of the period is invalid." +
                    " Expected format dd-MM-yyyy ex. 01-01-2020 translates to 1 Jan 2020");
        }
        return Optional.ofNullable(date);
    }

    @GetMapping("attendance/departments/{departmentId}/employees")
    public CollectionModel<EmployeeAttendanceResponseDTO> loadEmployeesByDepartment(
            @PathVariable("departmentId") Long departmentId) {
        List<EmployeeAttendanceResponseDTO> dtos = employeeService
                .getEmployeeByDepartmentId(departmentId)
                .stream()
                .map(attendanceResponseMapper::entityToDTO)
                .collect(Collectors.toList());

        return CollectionModel.of(dtos
                , linkTo(methodOn(EmployeeController.class).loadEmployeesByDepartment(departmentId)).withSelfRel());

    }
}
