package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserControllerAdvice {
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    String userNotFoundHandler(UserNotFoundException unfe){
        return unfe.getMessage();
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(UserLoginFailed.class)
    String loginFailed(UserLoginFailed ulf){ return ulf.getMessage();}
}
