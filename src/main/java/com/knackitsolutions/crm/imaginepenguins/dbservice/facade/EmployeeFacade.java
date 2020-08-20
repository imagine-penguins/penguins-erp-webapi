package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.DepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.PrivilegeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmployeeFacade {

    private static final Logger log = LoggerFactory.getLogger(EmployeeFacade.class);

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PrivilegeService privilegeService;

    public EmployeeCreationDTO newEmployee(EmployeeCreationDTO employeeDTO){

        Employee employee = convertToEmployee(employeeDTO);
        User newUser = userService.newUser(employee.getUser());

        Employee newEmployee = newUser.getEmployee();//employeeService.newEmployee(employee);
        if (newEmployee != null){
            log.info("New Employee: {}", newEmployee);

        }else{
            log.info("New Employee is null");
        }
        if (newEmployee.getUser() != null){
            log.info("New User: {}", newEmployee.getUser());
            if (newEmployee.getUser().getUserProfile() != null){
                log.info("New User Profile: {}", newEmployee.getUser().getUserProfile());
            }
        }

        return convertToEmployeeDTO(newEmployee);
    }

    public EmployeeCreationDTO oneEmployee(Long id){
        return convertToEmployeeDTO(employeeService.getOne(id)
                .orElseThrow(() -> {
                    return new EmployeeNotFoundException(id);
                })
        );
    }

    public List<EmployeeCreationDTO> allEmployee(){
        List<EmployeeCreationDTO> employeeCreationDTOList = employeeService.getAllEmployees()
                .stream()
                .map(employee -> {
                    return convertToEmployeeDTO(employee);
                })
                .collect(Collectors.toList());
        return employeeCreationDTOList;
    }

//    public Employee convertToEmployee(EmployeeCreationDTO dto){
//        user.setAdmin(dto.getAdmin());
//        Employee employee = modelMapper.map(dto, Employee.class);
//        return employee;
//    }

    public EmployeeCreationDTO convertToEmployeeDTO(Employee employee){
        EmployeeCreationDTO dto = new EmployeeCreationDTO();
        dto.setId(employee.getId());
        dto.setEmployeeTypeEnum(employee.getEmployeeType());
        dto.setEmployeeType(employee.getEmployeeType().getEmployeeTypeValue());
        if (employee.getEmployeeDepartments() != null) {
            dto.setDepartments(employee.getEmployeeDepartments()
                    .stream()
                    .map(i -> i.getInstituteDepartment().getId())
                    .collect(Collectors.toList())
            );
        }

        User user = employee.getUser();
        dto.setAdmin(user.getAdmin());
        dto.setSuperAdmin(user.getSuperAdmin());
        dto.setUsername(user.getUsername());
        if (user.getUserPrivileges() != null){
            dto.setPrivileges(user.getUserPrivileges()
                    .stream()
                    .map(i -> i.getPrivilege().getId())
                    .collect(Collectors.toList()));
        }

        UserProfile profile = user.getUserProfile();
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setAddressLine1(profile.getAddress().getAddressLine1());
        dto.setAddressLine2(profile.getAddress().getAddressLine2());
        dto.setCountry(profile.getAddress().getCountry());
        dto.setState(profile.getAddress().getState());
        dto.setZipcode(profile.getAddress().getZipcode());
        dto.setEmail(profile.getContact().getEmail());
        dto.setAlternateEmail(profile.getContact().getAlternateEmail());
        dto.setPhone(profile.getContact().getPhone());
        dto.setAlternatePhone(profile.getContact().getAlternatePhone());

        log.info("EMPLOYEE DTO: {}", dto);
        return dto;
    }

    private Employee convertToEmployee(EmployeeCreationDTO employeeDTO){
        Address address = new Address(employeeDTO.getAddressLine1(), employeeDTO.getAddressLine2(),
                employeeDTO.getState(), employeeDTO.getCountry(), employeeDTO.getZipcode());

        Contact contact = new Contact(employeeDTO.getPhone(), employeeDTO.getEmail(),
                employeeDTO.getAlternatePhone(), employeeDTO.getAlternateEmail());

        UserProfile userProfile = new UserProfile(employeeDTO.getFirstName(), employeeDTO.getLastName(), address, contact);

        User user = new User(employeeDTO.getId(), employeeDTO.getUsername(),
                employeeDTO.getPassword(), UserType.EMPLOYEE, employeeDTO.getAdmin(), employeeDTO.getSuperAdmin(), userProfile);

        userProfile.setUser(user);

        //setting user privileges

        List<UserPrivilege> userPrivilegeList = null;
        if (employeeDTO.getPrivileges() != null){
            userPrivilegeList = employeeDTO.getPrivileges()
                    .stream()
                    .map(id -> {
                        return new UserPrivilege(user, privilegeService.get(id));
                    })
                    .collect(Collectors.toList());
            if (userPrivilegeList != null){
                user.setUserPrivileges(userPrivilegeList);
            }
        }


        Employee employee = new Employee();
        employee.setUser(user);

        employee.setId(employeeDTO.getId());

        if (employeeDTO.getEmployeeType() != null){
            if(employeeDTO.getEmployeeType().equals('T'))
                employeeDTO.setEmployeeTypeEnum(EmployeeType.TEACHER);
            if(employeeDTO.getEmployeeType().equals('N'))
                employeeDTO.setEmployeeTypeEnum(EmployeeType.NON_TEACHER);
        }
        else{
            employeeDTO.setEmployeeTypeEnum(EmployeeType.TEACHER);
        }
        employee.setEmployeeType(employeeDTO.getEmployeeTypeEnum());

        //setting employee manager

        Optional<Employee> manager = null;
        if (employeeDTO.getManagerId() != null)
            manager = employeeService.getOne(employeeDTO.getManagerId());
        else
            log.info("Manager is id not given");

        if (manager != null && manager.isPresent())
            employee.setManager(manager.get());

        // departments

        if (employeeDTO.getDepartments() != null) {
            Set<EmployeeDepartment> departments = employeeDTO.getDepartments()
                    .stream()
                    .map(id -> {
                        return new EmployeeDepartment(employee, departmentRepository.getOne(id));
                    })
                    .collect(Collectors.toSet());
            if ( departments != null ){
                employee.setEmployeeDepartments(departments);
            }
        }
        user.setEmployee(employee);
        return employee;
    }



}
