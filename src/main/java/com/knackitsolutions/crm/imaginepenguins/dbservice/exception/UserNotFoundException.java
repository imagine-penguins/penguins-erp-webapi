package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id){
        super("UserDTO not found with id : " + id);
    }
}
