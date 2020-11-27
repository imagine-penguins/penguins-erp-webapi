package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.config.DatesConfig;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.UserAttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AttendanceRequestMapper {

    public Optional<Date> periodStartDateValue(Period period, Optional<String> value) {
        Date date = null;
        String v = value.orElseThrow(() -> new IllegalArgumentException("value of the period is not found"));
        try {
                date = period.startDate(v);
                log.debug("start date: {}", date);
        } catch (ParseException parseException) {
            throw new IllegalArgumentException("value of the period is invalid." +
                    " Expected format dd-MM-yyyy ex. 01-01-2020 translates to 1 Jan 2020");
        }
        return Optional.ofNullable(date);
    }
    public Optional<Date> periodEndDateValue(Period period, Optional<String> value) {
        Date date = null;
        String v = value.orElseThrow(() -> new IllegalArgumentException("value of the period is not found"));
        try {
                date = period.endDate(v);
                log.debug("end date: {}", date);
        } catch (ParseException parseException) {
            throw new IllegalArgumentException("value of the period is invalid." +
                    " Expected format dd-MM-yyyy ex. 01-01-2020 translates to 1 Jan 2020");
        }
        return Optional.ofNullable(date);
    }

    public StudentAttendance dtoToEntity(Attendance attendance, Student attendant) {
        if (attendance == null || attendant == null) {
            return null;
        }
        StudentAttendance studentAttendance = new StudentAttendance();
        studentAttendance.setAttendance(attendance);
        studentAttendance.setStudent(attendant);
        studentAttendance.setClassSection(attendant.getInstituteClassSection());
        studentAttendance.setStudentAttendanceKey(new StudentAttendanceKey(attendant.getId()
                , attendance.getId()));
        return studentAttendance;
    }

    public EmployeeAttendance dtoToEntity(Attendance attendance, Employee attendant) {
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setEmployee(attendant);
        employeeAttendance.setAttendance(attendance);
        employeeAttendance.setEmployeeAttendanceKey(new EmployeeAttendanceKey(attendant.getId()
                , attendance.getId()));
        return employeeAttendance;
    }
}
