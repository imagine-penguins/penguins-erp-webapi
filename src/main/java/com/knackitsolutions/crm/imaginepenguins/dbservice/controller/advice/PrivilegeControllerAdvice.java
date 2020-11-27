package com.knackitsolutions.crm.imaginepenguins.dbservice.controller.advice;

import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.PrivilegeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PrivilegeControllerAdvice {

    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(PrivilegeNotFoundException.class)
    public String privilegeNotFoundHandler(PrivilegeNotFoundException pnfe){return pnfe.getMessage();}
}
