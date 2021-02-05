package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDepartmentDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import org.springframework.stereotype.Component;

@Component
public class InstituteDepartmentMapper {
    public void entityToDTO(InstituteDepartment entity, InstituteDepartmentDTO dto) {
        if (entity == null || dto == null)
            return;
        dto.setDepartmentName(entity.getDepartmentName());
        dto.setId(entity.getId());
    }

    public InstituteDepartmentDTO entityToDTO(InstituteDepartment entity) {
        if (entity == null) {
            return null;
        }
        InstituteDepartmentDTO dto = new InstituteDepartmentDTO();
        entityToDTO(entity, dto);
        return dto;
    }
}
