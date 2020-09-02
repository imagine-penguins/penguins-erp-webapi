package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EmployeeResponseMapper extends UserResponseMapper {

    public void updateDTO(EmployeeLoginResponseDTO dto, Employee employee){
        if (employee == null || dto == null)
            return ;
        entityToDTO(dto, employee);
        dto.setDepartments(employee.getUserDepartments()
                .stream()
                .map(employeeDepartment -> employeeDepartment.getInstituteDepartment().getId())
                .collect(Collectors.toList()));
        dto.setInstituteName(findInstitute(employee).getName());
        dto.setInstituteId(findInstitute(employee).getId());
        dto.setEmployeeType(employee.getEmployeeType());
        dto.setManagerId(employee.getManager().getId());
        dto.setSubordinates(employee.getSubordinates()
                .stream()
                .map(employee1 -> employee1.getId())
                .collect(Collectors.toSet()));
    }

    public EmployeeLoginResponseDTO toDTO(Employee employee){
        if (employee == null)
            return null;
        EmployeeLoginResponseDTO dto = new EmployeeLoginResponseDTO();
        entityToDTO(dto, employee);
        dto.setDepartments(employee.getUserDepartments()
                .stream()
                .map(employeeDepartment -> employeeDepartment.getInstituteDepartment().getId())
                .collect(Collectors.toList()));
        dto.setInstituteName(findInstitute(employee).getName());
        dto.setInstituteId(findInstitute(employee).getId());
        dto.setEmployeeType(employee.getEmployeeType());
        dto.setManagerId(employee.getManager().getId());
        dto.setSubordinates(employee.getSubordinates()
                .stream()
                .map(employee1 -> employee1.getId())
                .collect(Collectors.toSet()));

        return dto;
    }

    private Institute findInstitute(Employee employee){
        return employee.getUserDepartments()
                .stream()
                .findFirst()
                .get()
                .getInstituteDepartment()
                .getInstitute();
    }
}
