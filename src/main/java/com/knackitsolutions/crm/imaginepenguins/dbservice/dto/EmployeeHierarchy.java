package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@JsonPropertyOrder({"firstName", "lastName", "middleName", "email", "phone", "designation"})
public class EmployeeHierarchy {

    @JsonIgnore
    private final Employee employee;

    public String getFirstName() {
        return this.employee.getUserProfile().getFirstName();

    }

    public String getMiddleName() {
        return this.employee.getUserProfile().getMiddleName();
    }

    public String getLastName() {
        return this.employee.getUserProfile().getFirstName();
    }

    public String getEmail() {
        return  this.employee.getUserProfile().getContact().getEmail();
    }

    public String getPhone() {
        return this.employee.getUserProfile().getContact().getPhone();
    }

    public String getDesignation() {
        return this.employee.getDesignation();
    }

}
