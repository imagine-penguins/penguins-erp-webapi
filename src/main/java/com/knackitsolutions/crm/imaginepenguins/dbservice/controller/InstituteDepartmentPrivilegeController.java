package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartmentPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserPrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/privileges")
@RequiredArgsConstructor
public class InstituteDepartmentPrivilegeController {
    private final InstituteDepartmentPrivilegeRepository privilegeRepository;
    private final UserPrivilegeRepository userPrivilegeRepository;

    @GetMapping("/institute-departments")
    public CollectionModel<InstituteDepartmentPrivilege> instituteDepartmentPrivileges() {
        return CollectionModel.of(privilegeRepository.findAll());
    }

    @GetMapping("/users")
    public CollectionModel<UserPrivilege> userPrivileges() {
        return CollectionModel.of(userPrivilegeRepository.findAll());
    }
}
