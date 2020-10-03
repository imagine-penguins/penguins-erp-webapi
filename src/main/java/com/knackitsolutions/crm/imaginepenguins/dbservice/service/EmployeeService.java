package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.AttendanceController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.UserAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeAttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeAttendanceRepository attendanceRepository;

    public Employee newEmployee(Employee employee){
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findById(Long id){
        return employeeRepository.findById(id);
    }

    public List<Employee> getAllEmployees(){return employeeRepository.findAll();}

    public void delete(Long id){
        employeeRepository.delete(findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id)));
    }


    public List<EmployeeAttendance> getEmployeeAttendancesByEmployeeId(Long employeeId, Optional<Date> startDate, Optional<Date> endDate) {
        if (startDate.isPresent() && endDate.isPresent()) {
            return attendanceRepository.findByEmployeeAttendanceKeyEmployeeIdAndAttendanceAttendanceDateBetween(employeeId, startDate.get(), endDate.get());
        }
        return attendanceRepository.findByEmployeeAttendanceKeyEmployeeId(employeeId);
    }

    public EmployeeAttendance getEmployeeAttendanceById(EmployeeAttendanceKey key) {
        return attendanceRepository
                .findById(key)
                .orElseThrow(() -> new RuntimeException("Employee Attendance Found."));
    }

    public Optional<EmployeeAttendance> saveAttendance(EmployeeAttendance employeeAttendance) {
        return Optional.ofNullable(attendanceRepository.save(employeeAttendance));

    }
}
