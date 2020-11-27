package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeAttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
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

    public List<Employee> getEmployeeByDepartmentId(Long departmentId) {
        return employeeRepository.findByUserDepartmentsInstituteDepartmentId(departmentId);
    }

    public List<Employee> listEmployeesWith(Integer instituteId, Optional<Boolean> active, Pageable pageable) {
        Specification<Employee> specification = employeesByInstituteId(instituteId);
        active
                .map(EmployeeService::employeesByActive)
                .ifPresent(specification::and);
        return employeeRepository.findAll(specification, pageable).toList();
    }

    public List<Employee> listEmployeesWith(Integer instituteId, Optional<Boolean> active) {
        Specification<Employee> specification = employeesByInstituteId(instituteId);
        specification = active
                .map(EmployeeService::employeesByActive)
                .map(specification::and)
                .orElse(specification);
        return employeeRepository.findAll(specification);
    }

    public static Specification<Employee> employeesByActive(Boolean active) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }

    public static Specification<Employee> employeesByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                        .join("institute", JoinType.LEFT)
                        .get("id")
                        , instituteId
                );
    }

}
