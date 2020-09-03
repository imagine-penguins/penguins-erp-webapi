package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AppDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.WebDashboardDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @GetMapping("/web/user/{userId}/department/{departmentId}")
    public EntityModel<WebDashboardDTO> webDashboardDTO(@PathVariable("userId") Long userId
            , @PathVariable("departmentId") Long departmentId){
        return null;
    }

    @GetMapping("/app/user/{userId}/department/{departmentId}")
    public EntityModel<AppDashboardDTO> appDashboardDTO(@PathVariable("userId") Long userId
            , @PathVariable("departmentId") Long departmentId){
        return null;
    }

}
