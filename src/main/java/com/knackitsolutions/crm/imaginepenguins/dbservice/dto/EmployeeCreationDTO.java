package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;

import java.util.List;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        @JsonSubTypes.Type(value = TeacherCreationDTO.class, name = "T")
)
public class EmployeeCreationDTO extends UserCreationDTO{

    public EmployeeCreationDTO() {
    }

    EmployeeType employeeType;

    Long managerId;

    List<Long> departments;

    Set<Long> subordinates;

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

    @Override
    public String toString() {
        super.toString();
        return "EmployeeCreationDTO{" +
                "employeeType=" + employeeType +
                ", employeeType=" + employeeType +
                ", managerId=" + managerId +
                ", departments=" + departments +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Set<Long> getSubordinates() {
        return subordinates;
    }

    @JsonIgnore
    public void setSubordinates(Set<Long> subordinates) {
        this.subordinates = subordinates;
    }
}
