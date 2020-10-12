package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.AttendanceRequestDTO;
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
import java.util.Optional;
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
        Attendance attendance = new Attendance(0l, date, status, supervisor);
        log.debug("Attendance :{}", attendance);
        return attendanceRepository.save(attendance);
    }

    private EmployeeAttendance dtoToEmployeeAttendanceEntity(AttendanceRequestDTO attendanceRequestDTO
            , AttendanceRequestDTO.UserAttendanceRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();

        employeeService
                .findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()))
                .setEmployeeAttendances(employeeAttendance);

        Attendance attendance = saveAttendance(attendanceRequestDTO.getAttendanceDate(), dto.getStatus()
                , userService.findById(attendanceRequestDTO.getSupervisorId()));

        attendance.setEmployeeAttendance(employeeAttendance);


        EmployeeAttendanceKey compositeKey = createEmployeeCompositeKey(dto.getUserId(), attendance.getId());

        employeeAttendance.setEmployeeAttendanceKey(compositeKey);

        return employeeAttendance;
    }

    private StudentAttendance dtoToStudentAttendanceEntity(AttendanceRequestDTO attendanceRequestDTO
            , AttendanceRequestDTO.UserAttendanceRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        StudentAttendance studentAttendance = new StudentAttendance();

        studentService
                .one(dto.getUserId())
                .setStudentAttendances(studentAttendance);

        Attendance attendance = saveAttendance(attendanceRequestDTO.getAttendanceDate(), dto.getStatus()
                , userService.findById(attendanceRequestDTO.getSupervisorId()));

        attendance.setStudentAttendance(studentAttendance);

        StudentAttendanceKey compositeKey = createStudentCompositeKey(dto.getUserId(), attendance.getId());

        studentAttendance.setStudentAttendanceKey(compositeKey);



        return studentAttendance;
    }

    public List<StudentAttendance> dtoToEntity(AttendanceRequestDTO dto, Optional<Long> classId, Optional<Long> subjectId) {
        return dto.getAttendanceData()
                .stream()
                .map(userData -> dtoToStudentAttendanceEntity(dto, userData))
                .map(studentAttendance -> setClassesOrSubjects(studentAttendance, classId, subjectId))
                .collect(Collectors.toList());

    }

    private StudentAttendance setClassesOrSubjects(StudentAttendance studentAttendance
            , Optional<Long> classId, Optional<Long> subjectId) {
        classId
                .ifPresent(id -> classSectionRepository.getOne(id).setStudentAttendances(studentAttendance));
        subjectId
                .ifPresent(id -> classSectionSubjectRepository.getOne(id).setStudentAttendance(studentAttendance));
        return studentAttendance;
    }

    public List<EmployeeAttendance> dtoToEntity(AttendanceRequestDTO dto, Optional<Long> departmentId) {
        return dto.getAttendanceData()
                .stream()
                .map(userData -> dtoToEmployeeAttendanceEntity(dto, userData))
                .map(employeeAttendance -> setDepartment(employeeAttendance, departmentId))
                .collect(Collectors.toList());

    }

    private EmployeeAttendance setDepartment(EmployeeAttendance employeeAttendance, Optional<Long> departmentId) {
        instituteDepartmentRepository.findById(departmentId
                .orElseThrow(() -> new RuntimeException("DepartmentId can not be null")))
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId))
                .setEmployeeAttendances(employeeAttendance);
        return employeeAttendance;
    }

}
