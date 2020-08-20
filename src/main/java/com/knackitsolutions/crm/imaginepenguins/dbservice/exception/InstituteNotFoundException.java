package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class InstituteNotFoundException extends RuntimeException{

    public InstituteNotFoundException(Integer id){
        super("Could not find institute with id " + id);
    }
}
