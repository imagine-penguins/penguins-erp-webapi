package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PrivilegeMapper {

    public void entityToDTO(Privilege entity, PrivilegeDTO dto){
        if (entity == null || dto == null)
            return;

        dto.setId(entity.getId());
        dto.setName(entity.getPrivilegeName());
        dto.setBgImg(entity.getLogo());
        Optional
                .ofNullable(entity.getPrivilegeCode().getLinks())
                .ifPresent(dto::add);


    }

    public PrivilegeDTO entityToDTO(Privilege entity){
        if (entity == null)
            return null;

        PrivilegeDTO dto = new PrivilegeDTO();
        entityToDTO(entity, dto);
        return dto;
    }

    public List<PrivilegeDTO> entityToDTO(List<Privilege> userPrivileges) {
        log.info("Creating DTO list from privileges");
        List<PrivilegeDTO> dtos = userPrivileges
                .stream()
                .map(privilege -> entityToDTO(privilege))
                .collect(Collectors.toList());
        return dtos;
    }
}
