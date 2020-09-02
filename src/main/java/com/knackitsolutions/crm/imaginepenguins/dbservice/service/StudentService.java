package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.StudentNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    StudentRepository repository;

    public Student one(Long id){
        return repository.findById(id)
                .orElseThrow(()->new StudentNotFoundException(id));
    }

    public List<Student> all(){
        return repository.findAll();
    }
}
