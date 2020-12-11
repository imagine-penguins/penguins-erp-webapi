package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserPrivilegeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppDashboardFacade {

    private final PrivilegeMapper privilegeMapper;

    private final UserPrivilegeRepository userPrivilegeRepository;

    //Get the department from UserDepartment
    // . Find all the privileges in instituteDepartment for that department
    // . Convert that to PrivilegeDTO. Make a list

    public List<PrivilegeDTO> getPrivileges(Long userId, Long departmentId){
        log.info("Preparing DTO list");
        List<Privilege> privileges = userPrivilegeRepository
                .findByUserIdAndDepartmentId(userId, departmentId)
                .stream()
                .map(userPrivilege -> userPrivilege.getDepartmentPrivilege().getPrivilege())
                .collect(Collectors.toList());
        privileges.forEach(privilege -> log.info("Privilege: {}", privilege));
        log.info("DTO list is prepared");
        return privilegeMapper.entityToDTO(privileges);
    }

}
