package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.TeacherNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher findById(Long id){
        return teacherRepository.findById(id)
                .orElseThrow(()->new TeacherNotFoundException(id));
    }
}
