package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.StudentLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    public StudentAttendanceResponseDTO mapStudentAttendanceToStudent(StudentAttendance studentAttendance) {


        StudentAttendanceResponseDTO dto = new StudentAttendanceResponseDTO();
        dto.setAttendanceId(Optional.ofNullable(studentAttendance.getStudentAttendanceKey().getAttendanceId()));
        dto.setUserId(studentAttendance.getStudentAttendanceKey().getStudentId());
        dto.setStatus(Optional.ofNullable(studentAttendance.getAttendance().getAttendanceStatus()));
        dto.setRollNumber(studentAttendance.getStudent().getRollNumber());
        dto.setFirstName(studentAttendance.getStudent().getUserProfile().getFirstName());
        dto.setLastName(studentAttendance.getStudent().getUserProfile().getLastName());
        dto.setAttendanceDate(studentAttendance.getAttendance().getAttendanceDate());
        return dto;
    }
}
