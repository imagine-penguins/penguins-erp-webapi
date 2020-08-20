package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(Long id){
        super("could not find employee with id: " + id);
    }
}
