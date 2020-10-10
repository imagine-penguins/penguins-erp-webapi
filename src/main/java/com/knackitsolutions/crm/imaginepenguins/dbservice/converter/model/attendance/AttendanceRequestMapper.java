package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.EmployeeAttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.UserAttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteClassSectionRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteClassSectionSubjectRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AttendanceRequestMapper {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private InstituteClassSectionRepository classSectionRepository;

    @Autowired
    private InstituteClassSectionSubjectRepository classSectionSubjectRepository;

    @Autowired
    private InstituteDepartmentRepository instituteDepartmentRepository;

    private StudentAttendanceKey createStudentCompositeKey(Long studentId, Long attendanceId) {
        StudentAttendanceKey compositeKey = new StudentAttendanceKey();
        compositeKey.setAttendanceId(attendanceId);
        compositeKey.setStudentId(studentId);
        return compositeKey;
    }

    private EmployeeAttendanceKey createEmployeeCompositeKey(Long employeeId, Long attendanceId) {
        EmployeeAttendanceKey compositeKey = new EmployeeAttendanceKey(employeeId, attendanceId);
        return compositeKey;
    }

    private Attendance saveAttendance(Date date, AttendanceStatus status, User supervisor) {
        Attendance attendance = new Attendance(1l, date, status, supervisor);
        log.debug("Attendance :{}", attendance);
        return attendanceRepository.save(attendance);
    }

    public EmployeeAttendance dtoToEntity(UserAttendanceRequestDTO dto, EmployeeAttendanceRequestDTO employeeAttendanceRequestDTO) {
        if (dto == null) {
            return null;
        }
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        employeeService
                .findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()))
                .setEmployeeAttendances(employeeAttendance);
        Attendance attendance = saveAttendance(employeeAttendanceRequestDTO.getAttendanceDate(), dto.getStatus()
                , userService.findById(employeeAttendanceRequestDTO.getSupervisorId()));
        attendance.setEmployeeAttendance(employeeAttendance);

        EmployeeAttendanceKey compositeKey = createEmployeeCompositeKey(dto.getUserId(), attendance.getId());

        employeeAttendance.setEmployeeAttendanceKey(compositeKey);

        if (employeeAttendanceRequestDTO.getDepartmentId() != null) {
            instituteDepartmentRepository.findById(employeeAttendanceRequestDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + employeeAttendanceRequestDTO.getDepartmentId()))
                    .setEmployeeAttendances(employeeAttendance);

        }
        return employeeAttendance;
    }
    public StudentAttendance dtoToEntity(UserAttendanceRequestDTO dto, StudentAttendanceRequestDTO studentAttendanceRequestDTO) {
        if (dto == null) {
            return null;
        }
        StudentAttendance studentAttendance = new StudentAttendance();

        studentService
                .one(dto.getUserId())
                .setStudentAttendances(studentAttendance);

        Attendance attendance = saveAttendance(studentAttendanceRequestDTO.getAttendanceDate(), dto.getStatus()
                , userService.findById(studentAttendanceRequestDTO.getSupervisorId()));

        attendance.setStudentAttendance(studentAttendance);

        StudentAttendanceKey compositeKey = createStudentCompositeKey(dto.getUserId(), attendance.getId());

        studentAttendance.setStudentAttendanceKey(compositeKey);

        if (studentAttendanceRequestDTO.getSubjectClassID() != null)
            classSectionSubjectRepository
                    .getOne(studentAttendanceRequestDTO.getSubjectClassID())
                    .setStudentAttendance(studentAttendance);

        if (studentAttendanceRequestDTO.getClassSectionId() != null)
            classSectionRepository
                    .getOne(studentAttendanceRequestDTO.getClassSectionId())
                    .setStudentAttendances(studentAttendance);

        return studentAttendance;
    }

    public List<StudentAttendance> dtoToEntity(StudentAttendanceRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return dto.getAttendanceData().stream().map(studentDto -> dtoToEntity(studentDto, dto)).collect(Collectors.toList());
    }

    public List<EmployeeAttendance> dtoToEntity(EmployeeAttendanceRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return dto.getAttendanceData()
                .stream()
                .map(employeeDTO -> dtoToEntity(employeeDTO, dto))
                .collect(Collectors.toList());
    }
}
