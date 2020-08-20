package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InstituteControllerAdvice {

    @ResponseBody
    @ExceptionHandler(InstituteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String instituteNotFoundHandler(InstituteNotFoundException infe){
        return infe.getMessage();
    }
}
