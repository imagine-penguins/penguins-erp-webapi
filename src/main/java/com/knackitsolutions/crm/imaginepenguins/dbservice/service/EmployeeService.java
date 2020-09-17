package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

}
