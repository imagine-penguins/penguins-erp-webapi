package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.StudentLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentFacade {

    @Autowired
    StudentLoginResponseMapper studentResponseMapper;

    @Autowired
    StudentServiceImpl service;

    public StudentLoginResponseDTO getOne(Long id){
        return studentResponseMapper.toDTO(service.one(id));
    }

    public List<StudentLoginResponseDTO> all(){
        return studentResponseMapper.toDTO(service.all());
    }
}
