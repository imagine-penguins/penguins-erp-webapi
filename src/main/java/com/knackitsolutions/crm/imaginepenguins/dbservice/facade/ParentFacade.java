package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ParentLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ParentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParentFacade {

    @Autowired
    ParentService parentService;

    @Autowired
    ParentLoginResponseMapper parentResponseMapper;

    public ParentLoginResponseDTO findById(Long id){
        return parentResponseMapper.parentResponseDTO(parentService.findById(id));
    }

    public List<ParentLoginResponseDTO> findAll(){
        return parentResponseMapper.parentResponseDTOList(parentService.parents());
    }
}
