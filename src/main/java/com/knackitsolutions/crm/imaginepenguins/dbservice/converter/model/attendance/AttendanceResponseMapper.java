package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.EmployeeAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
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

    @Autowired
    StudentService studentService;

    @Autowired
    EmployeeService employeeService;

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
    public List<StudentAttendanceResponseDTO> getStudentAttendanceResponseDTOList(Optional<Long> classId
            , Optional<Long> userId, Optional<Date> startDate, Optional<Date> endDate) {

        List<StudentAttendance> studentAttendances = classId.map(cid -> userId
                .map(sid -> studentService.getStudentAttendancesByStudentId(sid, startDate, endDate))
                .orElseGet(() -> studentService.getStudentAttendancesByClassId(cid, startDate, endDate)))
                .orElse(new ArrayList<>());

        studentAttendances.forEach(studentAttendance -> log.debug("Student Attendance: {}", studentAttendance));
        List<StudentAttendanceResponseDTO> students = studentAttendances.stream()
                .map(this::mapStudentAttendanceToStudent)
                .collect(Collectors.toList());

        return students;
    }

    public List<EmployeeAttendanceResponseDTO> getEmployeeDTOList(Optional<Long> departmentId, Optional<Long> userId
            , Optional<Date> startDate, Optional<Date> endDate) {

        List<EmployeeAttendance> employeeAttendances = departmentId.map(did -> userId.map(
                uid -> employeeService.getEmployeeAttendancesByEmployeeId(uid, startDate, endDate))
                .orElseGet(() -> employeeService.getEmployeeAttendancesByDepartmentId(did, startDate, endDate))
        ).orElse(new ArrayList<>());


        employeeAttendances.forEach(employeeAttendance -> log.debug("Student Attendance: {}", employeeAttendance));

        List<EmployeeAttendanceResponseDTO> employees = employeeAttendances.stream()
                .map(this::mapEmployeeAttendanceToEmployee)
                .collect(Collectors.toList());

        return employees;
    }

}
