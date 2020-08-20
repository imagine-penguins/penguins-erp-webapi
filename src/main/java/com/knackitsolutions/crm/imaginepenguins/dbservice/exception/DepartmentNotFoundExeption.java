package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class DepartmentNotFoundExeption extends RuntimeException{

    public DepartmentNotFoundExeption(Long id){
        super("Department Not Found on id: " + id);
    }

}
