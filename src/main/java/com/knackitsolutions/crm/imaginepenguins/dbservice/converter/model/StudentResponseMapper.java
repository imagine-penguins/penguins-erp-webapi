package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentResponseMapper extends UserResponseMapper{

    @Autowired
    InstituteClassSectionMapper instituteClassSectionMapper;

    public StudentLoginResponseDTO toDTO(Student student){
        if (student == null)
            return null;
        StudentLoginResponseDTO dto = new StudentLoginResponseDTO();

        entityToDTO(dto, student);

        dto.setParentId(student.getParent().getId());
        dto.setClasss(instituteClassSectionMapper.toDTO(student.getInstituteClassSectionTeacher()));

        return dto;
    }

    public List<StudentLoginResponseDTO> toDTO(List<Student> students){
        return students.stream()
                .map(student -> toDTO(student))
                .collect(Collectors.toList());
    }

}
