package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EmployeeType {
    TEACHER("T"),
    NON_TEACHER("N");

    @JsonProperty
    private String employeeTypeValue;

    EmployeeType(String employeeTypeValue) {
        this.employeeTypeValue = employeeTypeValue;
    }

    public String getEmployeeTypeValue() {
        return employeeTypeValue;
    }

    @JsonCreator
    public static EmployeeType of(@JsonProperty String employeeTypeValue){
        return Stream.of(EmployeeType.values())
                .filter(employeeType -> employeeType.getEmployeeTypeValue() == employeeTypeValue)
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
