package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AppDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.WebDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.AppDashboardFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @Autowired
    AppDashboardFacade appDashboardFacade;

    @GetMapping("/web/user/{userId}/department/{departmentId}")
    public EntityModel<WebDashboardDTO> webDashboardDTO(@PathVariable("userId") Long userId
            , @PathVariable("departmentId") Long departmentId){
        return null;
    }

    @GetMapping("/app/user/{userId}/department/{departmentId}")
    public EntityModel<AppDashboardDTO> appDashboardDTO(@PathVariable("userId") Long userId
            , @PathVariable("departmentId") Long departmentId){
        AppDashboardDTO dto = new AppDashboardDTO();
        List<PrivilegeDTO> privilegeDTOS = appDashboardFacade
                .getPrivileges(userId, departmentId);
        dto.setPrivileges(privilegeDTOS);

        //To Do add links for module functions

        return EntityModel.of(dto);
    }

}
