package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.UserAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AttendanceResponseMapper {

    public void entityToDTO(UserAttendanceResponseDTO dto, User entity) {
        dto.setUserId(entity.getId());
        dto.setFirstName(entity.getUserProfile().getFirstName());
        dto.setLastName(entity.getUserProfile().getLastName());
        dto.setProfilePic(
                entity
                        .getUserDocumentStores()
                        .stream()
                        .filter(userDocumentStore -> userDocumentStore.getDocumentType() == UserDocumentType.DISPLAY_PICTURE)
                        .findFirst()
                        .orElseGet(UserDocumentStore::new)
                        .getFileName()
        );
    }

    public UserAttendanceResponseDTO entityToDTO(Student entity) {
        if (entity == null) {
            return null;
        }
        UserAttendanceResponseDTO dto = new UserAttendanceResponseDTO();
        entityToDTO(dto, entity);
        dto.setRollNumber(entity.getRollNumber());
        return dto;
    }

    public UserAttendanceResponseDTO entityToDTO(Employee entity) {
        if (entity == null) {
            return null;
        }
        UserAttendanceResponseDTO dto = new UserAttendanceResponseDTO();
        entityToDTO(dto, entity);
        dto.setEmployeeId(entity.getEmployeeOrgId());
        return dto;
    }

    public UserAttendanceResponseDTO mapUserAttendanceToStudent(StudentAttendance studentAttendance) {
        if (studentAttendance == null) {
            return null;
        }

        UserAttendanceResponseDTO dto = new UserAttendanceResponseDTO();
        dto.setAttendanceId(Optional.ofNullable(studentAttendance.getStudentAttendanceKey().getAttendanceId()));
        dto.setUserId(studentAttendance.getStudentAttendanceKey().getStudentId());
        dto.setStatus(Optional.ofNullable(studentAttendance.getAttendance().getAttendanceStatus()));
        dto.setRollNumber(studentAttendance.getStudent().getRollNumber());
        dto.setFirstName(studentAttendance.getStudent().getUserProfile().getFirstName());
        dto.setLastName(studentAttendance.getStudent().getUserProfile().getLastName());
        dto.setAttendanceDate(studentAttendance.getAttendance().getAttendanceDate());
        return dto;
    }

    public UserAttendanceResponseDTO mapUserAttendanceToEmployee(EmployeeAttendance entity) {

        UserAttendanceResponseDTO dto = new UserAttendanceResponseDTO();
        dto.setAttendanceId(Optional.ofNullable(entity.getEmployeeAttendanceKey().getAttendanceId()));
        dto.setUserId(entity.getEmployeeAttendanceKey().getEmployeeId());
        dto.setStatus(Optional.ofNullable(entity.getAttendance().getAttendanceStatus()));
        dto.setFirstName(entity.getEmployee().getUserProfile().getFirstName());
        dto.setLastName(entity.getEmployee().getUserProfile().getLastName());
        dto.setAttendanceDate(entity.getAttendance().getAttendanceDate());
        dto.setEmployeeId(entity.getEmployee().getEmployeeOrgId());
        return dto;
    }

}
