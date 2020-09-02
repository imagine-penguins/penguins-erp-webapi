package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class ParentNotFoundException extends RuntimeException{
    public ParentNotFoundException(Long id){
        super("Parent not found with id: " + id);
    }
}
