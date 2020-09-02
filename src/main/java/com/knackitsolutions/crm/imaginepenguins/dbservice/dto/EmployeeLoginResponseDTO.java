package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import java.util.List;
import java.util.Set;

public class EmployeeLoginResponseDTO extends UserLoginResponseDTO {
    EmployeeType employeeType;

    Long managerId;

    List<Long> departments;

    Set<Long> subordinates;

    String instituteName;

    Integer instituteId;

    public EmployeeLoginResponseDTO() {
    }

    public EmployeeLoginResponseDTO(EmployeeType employeeType, Long managerId, List<Long> departments, Set<Long> subordinates, String instituteName, Integer instituteId) {
        this.employeeType = employeeType;
        this.managerId = managerId;
        this.departments = departments;
        this.subordinates = subordinates;
        this.instituteName = instituteName;
        this.instituteId = instituteId;
    }

//    public EmployeeLoginResponseDTO(Long id, String username, Boolean isAdmin, Boolean isSuperAdmin, UserType userType, UserProfileDTO profile, List<Integer> privileges, EmployeeType employeeType, Long managerId, List<Long> departments, Set<Long> subordinates, String instituteName, Integer instituteId) {
//        super(id, username, isAdmin, isSuperAdmin, userType, profile, privileges);
//        this.employeeType = employeeType;
//        this.managerId = managerId;
//        this.departments = departments;
//        this.subordinates = subordinates;
//        this.instituteName = instituteName;
//        this.instituteId = instituteId;
//    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public List<Long> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Long> departments) {
        this.departments = departments;
    }

    public Set<Long> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<Long> subordinates) {
        this.subordinates = subordinates;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }
}
