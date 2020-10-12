package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AppDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.WebDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.AppDashboardFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
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

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @GetMapping("/web/department/{departmentId}")
    public EntityModel<WebDashboardDTO> webDashboardDTO(@PathVariable("departmentId") Long departmentId){
        User user = (User) authenticationFacade.getAuthentication().getPrincipal();
        return null;
    }

    @GetMapping("/app/department/{departmentId}")
    public EntityModel<AppDashboardDTO> appDashboardDTO(@PathVariable("departmentId") Long departmentId){
        User user = (User) authenticationFacade.getAuthentication().getPrincipal();

        AppDashboardDTO dto = new AppDashboardDTO();
        List<PrivilegeDTO> privilegeDTOS = appDashboardFacade
                .getPrivileges(user.getId(), departmentId);
        dto.setPrivileges(privilegeDTOS);

        //To Do add links for module functions

        return EntityModel.of(dto);
    }

}
