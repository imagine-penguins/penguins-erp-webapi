package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.TeacherNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Teacher findById(Long id){
        return teacherRepository.findById(id)
                .orElseThrow(()->new TeacherNotFoundException(id));
    }

    @Override
    public Teacher newTeacher(Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        return teacherRepository.save(teacher);
    }

    @Override
    public List<InstituteClassSectionSubject> loadSubjectClasses(Long id) {
        return teacherRepository.getClassSectionSubjects(id);
    }

    @Override
    public List<InstituteClassSection> loadClassSections(Long id) {
        return teacherRepository.getClassSections(id);
    }
}
