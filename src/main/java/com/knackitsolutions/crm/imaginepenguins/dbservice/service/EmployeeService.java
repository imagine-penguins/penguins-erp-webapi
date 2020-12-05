package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeAttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    public Long count(Specification<Employee> specification) {
        return employeeRepository.count(specification);
    }

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
        Specification<Employee> specification = EmployeeSpecification.employeesByInstituteId(instituteId);
        return employeeRepository.findAll(specification, pageable).toList();
    }

    public List<Employee> listEmployeesWith(Integer instituteId, Optional<Boolean> active) {
        Specification<Employee> specification = EmployeeSpecification.employeesByInstituteId(instituteId);
        specification = active
                .map(EmployeeSpecification::employeesByActiveStatus)
                .map(specification::and)
                .orElse(specification);
        return employeeRepository.findAll(specification);
    }

    public Page<Employee> findAll(Specification<Employee> spec, Pageable pageable) {
        return employeeRepository.findAll(spec, pageable);
    }
}
