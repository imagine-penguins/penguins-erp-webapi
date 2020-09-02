package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class TeacherNotFoundException extends RuntimeException{
    public TeacherNotFoundException(Long id){
        super("teacher not found with id: " + id);
    }
}
