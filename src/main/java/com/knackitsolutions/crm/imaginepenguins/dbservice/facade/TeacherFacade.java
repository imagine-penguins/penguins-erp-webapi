package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.TeacherLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeacherFacade {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherLoginResponseMapper teacherLoginResponseMapper;

    public TeacherLoginResponseDTO findById(Long id){
        return teacherLoginResponseMapper.teacherEntityToTeacherDTO(teacherService.findById(id));
    }
}
