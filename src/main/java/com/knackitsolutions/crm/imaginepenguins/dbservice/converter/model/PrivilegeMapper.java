package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
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

//    public List<PrivilegeDTO> entityToDTORecus(List<Privilege> userPrivileges) {
//
//    }
    public List<PrivilegeDTO> entityToDTO(Collection<Privilege> userPrivileges) {
        log.info("Creating DTO list from privileges");
        List<PrivilegeDTO> dtos = userPrivileges
                .stream()
                .map(privilege -> entityToDTO(privilege))
                .collect(Collectors.toList());
        log.info("DTO list is prepared.");
        return dtos;
    }

    //To find the root parent privilege
    private Privilege getRootParent(Privilege privilege) {
        log.info("Privilege: {}", privilege);
        log.info("Finding the root parent.");
        Privilege parentPrivilege = null;
        while (privilege.getParentPrivilege() != null) {
            parentPrivilege = privilege.getParentPrivilege();
            log.info("Parent Privilege Found: {}", parentPrivilege);
            privilege = parentPrivilege;

        }
        log.info("root parent found. Privilege: {}", parentPrivilege);
        return parentPrivilege;
    }

    private void fromPrivilege(Privilege parentPrivilege, PrivilegeDTO dto, List<Privilege> userPrivileges) {
        if (dto == null || parentPrivilege == null || userPrivileges.isEmpty())
            return ;
        log.info("Recusively creating DTO list.");
        //list of sibling privileges
        List<PrivilegeDTO> childDTOs = new ArrayList<>();
        for (Privilege privilege : parentPrivilege.getPrivileges()) {
            if ( userPrivileges.contains(privilege) ){
                log.info("Parent Privilege Found.");
                userPrivileges.remove(privilege);
                PrivilegeDTO childDTO = null;
                if(!privilege.getPrivileges().isEmpty()){
                    fromPrivilege(privilege, new PrivilegeDTO(), userPrivileges);
                }
                childDTO = entityToDTO(privilege);
                childDTOs.add(childDTO);
            }
        }
        log.info("Updating DTO");
        entityToDTO(parentPrivilege, dto);
        log.info("DTO updated. DTO: {}", dto);
        log.info("DTO list.");
    }
}
