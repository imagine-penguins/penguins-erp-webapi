package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.AttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteClassSectionRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteClassSectionSubjectRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
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
public class AttendanceRequestMapper {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private InstituteClassSectionRepository classSectionRepository;

    @Autowired
    private InstituteClassSectionSubjectRepository classSectionSubjectRepository;

    private StudentAttendanceKey createCompositeKey(Long studentId, Long attendanceId) {
        StudentAttendanceKey compositeKey = new StudentAttendanceKey();
        compositeKey.setAttendanceId(attendanceId);
        compositeKey.setStudentId(studentId);
        return compositeKey;
    }

    private Attendance saveAttendance(Date date, AttendanceStatus status, User supervisor) {
        Attendance attendance = new Attendance(1l, date, status);
        attendance.setUser(supervisor);
        return attendanceRepository.save(attendance);
    }

    public StudentAttendance dtoToEntity(StudentAttendanceRequestDTO dto, AttendanceRequestDTO attendanceRequestDTO) {
        if (dto == null) {
            return null;
        }
        StudentAttendance studentAttendance = new StudentAttendance();

        studentService.one(dto.getStudentId()).setStudentAttendances(studentAttendance);

        Attendance attendance = saveAttendance(attendanceRequestDTO.getDate(), dto.getStatus()
                , userService.findById(attendanceRequestDTO.getSupervisorId()));

        attendance.setStudentAttendance(studentAttendance);

        StudentAttendanceKey compositeKey = createCompositeKey(dto.getStudentId(), attendance.getId());

        studentAttendance.setStudentAttendanceKey(compositeKey);

        if (attendanceRequestDTO.getSubjectClassID() != null)
            classSectionSubjectRepository
                    .getOne(attendanceRequestDTO.getSubjectClassID())
                    .setStudentAttendance(studentAttendance);

        if (attendanceRequestDTO.getClassSectionId() != null)
            classSectionRepository
                    .getOne(attendanceRequestDTO.getClassSectionId())
                    .setStudentAttendances(studentAttendance);

        return studentAttendance;
    }

    public List<StudentAttendance> dtoToEntity(AttendanceRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return dto.getAttendanceData().stream().map(studentDto -> dtoToEntity(studentDto, dto)).collect(Collectors.toList());
    }
}
