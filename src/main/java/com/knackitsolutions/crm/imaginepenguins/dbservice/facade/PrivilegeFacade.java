package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrivilegeFacade {
    @Autowired
    PrivilegeService privilegeService;

    public PrivilegeDTO newPrivilege(PrivilegeDTO dto){
        return convertToPrivilegeDTO(privilegeService.save(convertToPrivilege(dto)));
    }

    public List<PrivilegeDTO> allPrivileges(){
        return privilegeService.all()
                .stream()
                .map(privilege -> convertToPrivilegeDTO(privilege))
                .collect(Collectors.toList());
    }

    private PrivilegeDTO convertToPrivilegeDTO(Privilege privilege){
        return new PrivilegeDTO(privilege.getId(), privilege.getPrivilegeName(), privilege.getPrivilegeDesc());
    }

    private Privilege convertToPrivilege(PrivilegeDTO dto){
//        return new Privilege(dto.getPrivilegeName(), dto.getPrivilegeDesc());
        return null;
    }

    public PrivilegeDTO one(Integer id){
        return convertToPrivilegeDTO(privilegeService.get(id));
    }
}
