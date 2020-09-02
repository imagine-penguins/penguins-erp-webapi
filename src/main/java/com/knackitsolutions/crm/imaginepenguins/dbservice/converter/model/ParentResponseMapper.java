package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ParentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Parent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParentResponseMapper extends UserResponseMapper {

    public ParentLoginResponseDTO parentResponseDTO(Parent parent){
        if (parent == null)
            return null;

        ParentLoginResponseDTO dto = new ParentLoginResponseDTO();

        return dto;
    }

    public List<ParentLoginResponseDTO> parentResponseDTOList(List<Parent> parents){
        return parents.stream().map(parent -> parentResponseDTO(parent)).collect(Collectors.toList());
    }
}
