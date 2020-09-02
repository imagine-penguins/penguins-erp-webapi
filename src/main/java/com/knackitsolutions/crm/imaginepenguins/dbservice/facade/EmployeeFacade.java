package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.EmployeeMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.EmployeeResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.DepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.PrivilegeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeFacade {

    private static final Logger log = LoggerFactory.getLogger(EmployeeFacade.class);

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private EmployeeResponseMapper employeeResponseMapper;

    @Autowired
    private EmployeeMapperImpl employeeMapperImpl;

    public EmployeeLoginResponseDTO newEmployee(EmployeeCreationDTO employeeDTO){

        Employee employee = employeeMapperImpl.toEmployee(employeeDTO);
//                convertToEmployee(employeeDTO);
//        User newUser = userService.newUser(employee.getUser());

        Employee newEmployee = employeeService.newEmployee(employee);

//        Employee newEmployee = newUser.getEmployee();//employeeService.newEmployee(employee);
        log.info("New Employee: {}", newEmployee);

        return employeeResponseMapper.toDTO(newEmployee);
    }

    public EmployeeLoginResponseDTO oneEmployee(Long id){
        return employeeResponseMapper.toDTO(employeeService.getOne(id)
                .orElseThrow(() -> {
                    return new EmployeeNotFoundException(id);
                }));
    }

    public List<EmployeeLoginResponseDTO> allEmployee(){
        List<EmployeeLoginResponseDTO> employeeResponseDTOList = employeeService.getAllEmployees()
                .stream()
                .map(employee -> employeeResponseMapper.toDTO(employee))
                .collect(Collectors.toList());
        return employeeResponseDTOList;
    }

}
