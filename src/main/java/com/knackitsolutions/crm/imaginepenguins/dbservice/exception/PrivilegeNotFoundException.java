package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class PrivilegeNotFoundException extends RuntimeException{
    public PrivilegeNotFoundException(Integer id){super("Privilage not found with Id: " + id);}
}
