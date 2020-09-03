package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeacherLoginResponseMapper extends EmployeeLoginResponseMapper {

    @Autowired
    InstituteClassSectionMapper instituteClassSectionMapper;

    public TeacherLoginResponseDTO teacherEntityToTeacherDTO(Teacher teacher){
        if (teacher == null)
            return null;
        TeacherLoginResponseDTO dto = new TeacherLoginResponseDTO();
        updateDTO(dto, teacher);
        dto.setClasses(instituteClassSectionMapper.toDTO(teacher.getInstituteClassSections()));


        return dto;
    }
}
