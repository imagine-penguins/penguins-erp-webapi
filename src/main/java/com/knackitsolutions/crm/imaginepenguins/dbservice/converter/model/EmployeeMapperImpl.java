package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteDepartmentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeMapperImpl {

    private final UserProfileMapperImpl userProfileMapper;
    private final ContactMapperImpl contactMapper;
    private final UserMapperImpl userMapper;

    public EmployeeCreationDTO toDTO(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeCreationDTO employeeCreationDTO = new EmployeeCreationDTO();

        userMapper.userToUserCreationDTO(employeeCreationDTO, employee);

//        employeeCreationDTO.setPrivileges( userPrivilegesToPrivileges( employee.getUserPrivileges() ) );
//        employeeCreationDTO.setProfile( userProfileMapper.userProfileToDTO( employee.getUserProfile() ) );
        employeeCreationDTO.setManagerId( idFromEmployee( employee.getManager() ) );
        employeeCreationDTO.setSubordinates( employeeSetToLongSet( employee.getSubordinates() ) );
//        employeeCreationDTO.setUsername( employee.getUsername() );
//        employeeCreationDTO.setPassword( employee.getPassword() );
//        employeeCreationDTO.setAdmin( employee.getAdmin() );
//        employeeCreationDTO.setSuperAdmin( employee.getSuperAdmin() );
//        employeeCreationDTO.setUserType( employee.getUserType() );
//        employeeCreationDTO.setId( employee.getId() );
        employeeCreationDTO.setEmployeeType( employee.getEmployeeType() );

        setEmployeeDTODepartments( employeeCreationDTO, employee );

        return employeeCreationDTO;
    }

    public Employee toEmployee(EmployeeCreationDTO employeeCreationDTO) {
        if ( employeeCreationDTO == null ) {
            return null;
        }

        Employee employee = new Employee();

        userMapper.userCreationDTOToUser(employee, employeeCreationDTO);
//        employee.setManager( fromId( employeeCreationDTO.getManagerId() ) );
        employee.setSubordinates( longSetToEmployeeSet( employeeCreationDTO.getSubordinates() ) );
//        employee.setUserProfile( userProfileMapper.dtoToUserProfile( employeeCreationDTO.getProfile() ) );
//        employee.setId( employeeCreationDTO.getId() );
//        employee.setUsername( employeeCreationDTO.getUsername() );
//        employee.setUserType( employeeCreationDTO.getUserType() );
//        employee.setAdmin( employeeCreationDTO.getAdmin() );
//        employee.setSuperAdmin( employeeCreationDTO.getSuperAdmin() );
//        employee.setPassword( employeeCreationDTO.getPassword() );
        employee.setEmployeeType( employeeCreationDTO.getEmployeeType() );

//        userPrivileges( employeeCreationDTO, employee );
//        setEmployeeDepartments( employeeCreationDTO, employee );

        return employee;
    }

    protected Set<Long> employeeSetToLongSet(Set<Employee> set) {
        if ( set == null ) {
            return null;
        }

        Set<Long> set1 = new HashSet<Long>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Employee employee : set ) {
            set1.add( idFromEmployee( employee ) );
        }

        return set1;
    }

    protected Set<Employee> longSetToEmployeeSet(Set<Long> set) {
        if ( set == null ) {
            return null;
        }

        Set<Employee> set1 = new HashSet<Employee>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
//        for ( Long long1 : set ) {
//            set1.add( fromId( long1 ) );
//        }

        return set1;
    }

//    protected void setEmployeeDepartments(EmployeeCreationDTO creationDTO, Employee employee){
//        employee.setUserDepartments(creationDTO.getDepartments().stream().map(
//                id -> new UserDepartment(employee, departmentService.findOneByDepartmentId(id))
//        ).collect(Collectors.toSet()));
//    }
    protected  void setEmployeeDTODepartments(EmployeeCreationDTO creationDTO, Employee employee){
        creationDTO.setDepartments(employee.getUserDepartments()
                .stream()
                .map(employeeDepartment -> employeeDepartment.getInstituteDepartment().getId())
                .collect(Collectors.toList()));
    }
    protected Long idFromEmployee(Employee employee){
        return employee.getId();
    }
//    protected Employee fromId(Long id){
//        return employeeService.findById(id).orElseThrow(()->new EmployeeNotFoundException(id));
//    }
}
