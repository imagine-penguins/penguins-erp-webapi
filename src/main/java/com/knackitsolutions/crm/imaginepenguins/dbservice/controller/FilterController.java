package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/filters")
@Slf4j
@RequiredArgsConstructor
public class FilterController {

    private final IAuthenticationFacade authenticationFacade;
    private final UserDepartmentRepository userDepartmentRepository;
    private final UserPrivilegeRepository userPrivilegeRepository;
    private final InstituteClassSectionRepository instituteClassSectionRepository;
    private final InstituteClassSectionSubjectRepository instituteClassSectionSubjectRepository;
    private final PrivilegeRepository privilegeRepository;

    @GetMapping
    public EntityModel<Browse> all() {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        Browse browse = new Browse();

        List<UserType> userTypes = Arrays.asList(UserType.values());
        List<Object> departments = userDepartmentRepository
                .findByUserId(userContext.getUserId())
                .stream()
                .map(userDepartment -> {
                    return new Object(userDepartment.getId(), userDepartment.getInstituteDepartment().getDepartmentName());
                }).collect(Collectors.toList());
        List<Object> roles = privilegeRepository
                .findAll()
                .stream()
                .map(privilege -> {
                    return new Object(privilege.getId().longValue(), privilege.getPrivilegeName());
                }).collect(Collectors.toList());

        List<Object> classes = instituteClassSectionRepository
                .findByTeacherId(userContext.getUserId())
                .stream()
                .map(instituteClassSection -> {
                    return new Object(instituteClassSection.getId()
                            , instituteClassSection.getInstituteClass().getClasss().getClassName() + " " + instituteClassSection.getSection().getSectionName());
                }).collect(Collectors.toList());
        List<Object> subjects = instituteClassSectionSubjectRepository
                .findByTeacherId(userContext.getUserId())
                .stream()
                .map(instituteClassSectionSubject -> {
                    return new Object(instituteClassSectionSubject.getId()
                            , instituteClassSectionSubject
                            .getInstituteClassSection()
                            .getInstituteClass()
                            .getClasss()
                            .getClassName()
                            + " " +
                            instituteClassSectionSubject
                                    .getInstituteClassSection()
                                    .getSection()
                                    .getSectionName()
                            + " " +
                            instituteClassSectionSubject.getSubject().getName()
                    );
                }).collect(Collectors.toList());

        browse.setDepartments(departments);
        browse.setUserTypes(userTypes);
        browse.setRoles(roles);
        browse.setClasses(classes);
        browse.setSubjects(subjects);

        return EntityModel.of(browse);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Browse {
        private List<UserType> userTypes;
        private List<Object> departments;
        private List<Object> roles;
        private List<Object> classes;
        private List<Object> subjects;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Object {
        Long id;
        String name;
    }

}
