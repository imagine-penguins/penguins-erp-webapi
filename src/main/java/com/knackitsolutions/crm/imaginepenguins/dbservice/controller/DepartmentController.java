package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDepartmentDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final UserDepartmentRepository userDepartmentRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final PrivilegeMapper privilegeMapper;

    @GetMapping
    public CollectionModel<EntityModel<InstituteDepartmentDTO>> myDepartments(){
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();

        List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(userContext.getUserId());
        List<EntityModel<InstituteDepartmentDTO>> dtos = userDepartments.stream().map(userDepartment -> {
            InstituteDepartmentDTO dto = new InstituteDepartmentDTO();

            dto.setDepartmentName(userDepartment.getInstituteDepartment().getDepartmentName());
            dto.setId(userDepartment.getInstituteDepartment().getId());
            dto.setInstituteId(userDepartment.getInstituteDepartment().getInstitute().getId());
            dto.setPrivileges(userDepartment.getInstituteDepartment()
                    .getPrivileges()
                    .stream()
                    .map(instituteDepartmentPrivilege -> {
                        try {
                            return privilegeMapper.entityToDTO(instituteDepartmentPrivilege.getPrivilege());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .collect(Collectors.toList()));

            return EntityModel.of(dto); //Add Links To Department
        }).collect(Collectors.toList());

        return CollectionModel.of(dtos);
    }
}
