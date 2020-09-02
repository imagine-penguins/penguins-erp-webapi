package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.TeacherResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeacherFacade {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherResponseMapper mapper;

    public TeacherLoginResponseDTO findById(Long id){
        return mapper.teacherEntityToTeacherDTO(teacherService.findById(id));
    }
}
