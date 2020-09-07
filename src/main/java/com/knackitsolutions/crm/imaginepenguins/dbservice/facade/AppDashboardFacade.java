package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AppDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartmentPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppDashboardFacade {

    @Autowired
    private PrivilegeMapper privilegeMapper;

    //Get the department from UserDepartment. Find all the privileges in instituteDepartment for that department. Convert that to PrivilegeDTO. Make a list

    @Autowired
    private UserPrivilegeRepository userPrivilegeRepository;

    public List<PrivilegeDTO> getPrivileges(Long userId, Long departmentId){

        return userPrivilegeRepository
                .findByUserIdAndDepartmentId(userId, departmentId)
                .stream()
                .map(privilege -> privilegeMapper.entityToDTO(privilege.getDepartmentPrivilege().getPrivilege()))
                .collect(Collectors.toList());
    }


}
