package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import org.springframework.stereotype.Component;

@Component
public class PrivilegeMapper {

    public void entityToDTO(Privilege entity, PrivilegeDTO dto){
        if (entity == null || dto == null)
            return;

        dto.setId(entity.getId());
        dto.setName(entity.getPrivilegeName());
        dto.setLogo(entity.getLogo());
    }

    public PrivilegeDTO entityToDTO(Privilege entity){
        if (entity == null)
            return null;

        PrivilegeDTO dto = new PrivilegeDTO();
        entityToDTO(entity, dto);
        return dto;
    }
}
