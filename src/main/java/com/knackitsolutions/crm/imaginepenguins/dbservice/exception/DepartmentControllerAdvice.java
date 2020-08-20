package com.knackitsolutions.crm.imaginepenguins.dbservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DepartmentControllerAdvice {
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(DepartmentNotFoundExeption.class)
    public String departmentNotFoundHandler(DepartmentNotFoundExeption dnfe){return dnfe.getMessage();}
}
