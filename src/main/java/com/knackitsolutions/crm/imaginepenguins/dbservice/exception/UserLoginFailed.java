package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

public class UserLoginFailed extends RuntimeException{
    public UserLoginFailed(String username){
        super("user not found with username: " + username);
    }
}
