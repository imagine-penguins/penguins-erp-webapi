package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.AttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeAttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.StudentAttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    private final StudentAttendanceRepository studentAttendanceRepository;

    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public Optional<StudentAttendance> saveAttendance(StudentAttendance studentAttendance) {
        return Optional.ofNullable(studentAttendanceRepository.save(studentAttendance));
    }

    public List<StudentAttendance> saveStudentAttendance(List<StudentAttendance> studentAttendances) {
        return studentAttendanceRepository.saveAll(studentAttendances);
    }


    public List<StudentAttendance> getStudentAttendancesByClassId(Long classId) {
        return studentAttendanceRepository.findByClassSectionId(classId);
    }

    public List<StudentAttendance> getStudentAttendancesByClassId(Long classId
            , Optional<Date> updateTimeStart, Optional<Date> updateTimeEnd) {
        log.debug("Student Attendances for classId: {}", classId);
        if (updateTimeStart.isPresent() && updateTimeEnd.isPresent()) {
            log.debug("Lower and upper dates are present. calling DB for students attendances on period");
            return studentAttendanceRepository.findByClassSectionIdAndAttendanceAttendanceDateBetween(classId
                    , updateTimeStart.get(), updateTimeEnd.get());
        }
        log.debug("Period is not provided. Fetching students Attendances for classId");
        return getStudentAttendancesByClassId(classId);
    }

    public List<StudentAttendance> getStudentAttendancesByStudentId(Long studentId) {
        return studentAttendanceRepository.findByStudentAttendanceKeyStudentId(studentId);
    }

    public List<StudentAttendance> getStudentAttendancesByStudentId(Long studentId
            , Optional<LocalDateTime> updateTimeStart, Optional<LocalDateTime> updateTimeEnd) {
        if (updateTimeStart.isPresent() && updateTimeEnd.isPresent())
            return studentAttendanceRepository.findByStudentAttendanceKeyStudentIdAndAttendanceAttendanceDateBetween(studentId
                    , updateTimeStart.get(), updateTimeEnd.get());
        return getStudentAttendancesByStudentId(studentId);
    }

    public StudentAttendance getStudentAttendanceById(StudentAttendanceKey studentAttendanceKey) {
        return studentAttendanceRepository
                .findById(studentAttendanceKey)
                .orElseThrow(() -> new RuntimeException("Student Attendance Not Found."));
    }

    public List<StudentAttendance> getStudentAttendanceByClassSubjectId(Long subjectClassId) {
        return studentAttendanceRepository.findByInstituteClassSectionSubjectId(subjectClassId);
    }

    public List<StudentAttendance> getStudentAttendanceByClassSubjectId(Long subjectClassId, Date updateTimeStart, Date updateTimeEnd) {
        return studentAttendanceRepository.findByInstituteClassSectionSubjectIdAndAttendanceAttendanceDateBetween(subjectClassId
                , updateTimeStart, updateTimeEnd);
    }

    public List<EmployeeAttendance> getEmployeeAttendancesByEmployeeId(Long employeeId, Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate) {
        if (startDate.isPresent() && endDate.isPresent()) {
            return employeeAttendanceRepository.findByEmployeeAttendanceKeyEmployeeIdAndAttendanceAttendanceDateBetween(employeeId, startDate.get(), endDate.get());
        }
        return employeeAttendanceRepository.findByEmployeeAttendanceKeyEmployeeId(employeeId);
    }

    public EmployeeAttendance getEmployeeAttendanceById(EmployeeAttendanceKey key) {
        return employeeAttendanceRepository
                .findById(key)
                .orElseThrow(() -> new RuntimeException("Employee Attendance Found."));
    }

    public Optional<EmployeeAttendance> saveAttendance(EmployeeAttendance employeeAttendance) {
        return Optional.ofNullable(employeeAttendanceRepository.save(employeeAttendance));

    }

    public List<EmployeeAttendance> saveEmployeeAttendance(List<EmployeeAttendance> employeeAttendances) {
        return employeeAttendanceRepository.saveAll(employeeAttendances);

    }

    public List<EmployeeAttendance> getEmployeeAttendancesByDepartmentId(Long departmentId
            , Optional<Date> startDate, Optional<Date> endDate) {
        return employeeAttendanceRepository.findByEmployeeUserDepartmentsInstituteDepartmentId(departmentId);
    }

    public List<Attendance> findAll(Specification<Attendance> attendanceSpecification, Sort sort) {
        return attendanceRepository.findAll(attendanceSpecification, sort);
    }

    public LocalDateTime lastAttendanceDate() {
        return attendanceRepository.findLastAttendanceDate();
    }
}
