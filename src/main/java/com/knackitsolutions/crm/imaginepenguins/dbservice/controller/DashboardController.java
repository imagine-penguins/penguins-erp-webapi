package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AppDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.WebDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.AppDashboardFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AppDashboardFacade appDashboardFacade;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping("/web/department/{departmentId}")
    public EntityModel<WebDashboardDTO> webDashboardDTO(@PathVariable("departmentId") Long departmentId){
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        return null;
    }

    @GetMapping("/app/department/{departmentId}")
    public CollectionModel<PrivilegeDTO> appDashboardDTO(@PathVariable("departmentId") Long departmentId){
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        AppDashboardDTO dto = new AppDashboardDTO();
        List<PrivilegeDTO> privilegeDTOS = appDashboardFacade
                .getPrivileges(userContext.getUserId(), departmentId);
        //TODO add links for module functions

        return CollectionModel.of(privilegeDTOS);
    }

}
