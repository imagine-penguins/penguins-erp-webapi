package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.StudentLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.EmployeeAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AttendanceResponseMapper {
    public StudentAttendanceResponseDTO entityToDTO(Student entity) {
        if (entity == null) {
            return null;
        }
        StudentAttendanceResponseDTO dto = new StudentAttendanceResponseDTO();
        dto.setUserId(entity.getId());
        dto.setFirstName(entity.getUserProfile().getFirstName());
        dto.setLastName(entity.getUserProfile().getLastName());
        dto.setRollNumber(entity.getRollNumber());
        dto.setProfilePic(entity.getUserProfile().getProfilePic());
        return dto;
    }

    public EmployeeAttendanceResponseDTO entityToDTO(Employee entity) {
        if (entity == null) {
            return null;
        }
        EmployeeAttendanceResponseDTO dto = new EmployeeAttendanceResponseDTO();
        dto.setFirstName(entity.getUserProfile().getFirstName());
        dto.setLastName(entity.getUserProfile().getLastName());
        dto.setUserId(entity.getId());
        dto.setProfilePic(entity.getUserProfile().getProfilePic());
        return dto;
    }

    public StudentAttendanceResponseDTO mapStudentAttendanceToStudent(StudentAttendance studentAttendance) {
        if (studentAttendance == null) {
            return null;
        }


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

    public EmployeeAttendanceResponseDTO mapEmployeeAttendanceToEmployee(EmployeeAttendance entity) {

        EmployeeAttendanceResponseDTO dto = new EmployeeAttendanceResponseDTO();
        dto.setAttendanceId(Optional.ofNullable(entity.getEmployeeAttendanceKey().getAttendanceId()));
        dto.setUserId(entity.getEmployeeAttendanceKey().getEmployeeId());
        dto.setStatus(Optional.ofNullable(entity.getAttendance().getAttendanceStatus()));
        dto.setFirstName(entity.getEmployee().getUserProfile().getFirstName());
        dto.setLastName(entity.getEmployee().getUserProfile().getLastName());
        dto.setAttendanceDate(entity.getAttendance().getAttendanceDate());
        return dto;
    }
}
