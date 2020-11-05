package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@JsonPropertyOrder({"self", "manager"})
public class EmployeeHierarchy {

    @JsonIgnore
    private final Employee employee;

    public EmployeeDetail getManager() {
        return new EmployeeDetail(employee.getManager());
    }

    public EmployeeDetail getSelf() {
        return new EmployeeDetail(employee);
    }


    @Value
    @JsonPropertyOrder({"email", "phone", "designation"})
    public static class EmployeeDetail {

        @JsonIgnore
        private final Employee employee;

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

}
