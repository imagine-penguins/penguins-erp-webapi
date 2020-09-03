package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class UserLoginFailed extends RuntimeException{
    public UserLoginFailed(String username){
        super("username or password is invalid.");
    }
}
