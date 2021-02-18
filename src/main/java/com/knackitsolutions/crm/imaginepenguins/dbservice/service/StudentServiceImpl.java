package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.StudentNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.StudentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.StudentSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Student one(Long id){
        return studentRepository.findById(id)
                .orElseThrow(()->new StudentNotFoundException(id));
    }

    public List<Student> all(){
        return studentRepository.findAll();
    }

    public Long count(Specification<Student> specification) {
        return studentRepository.count(specification);
    }

    @Override
    public List<Student> loadStudentWithClassSectionId(Long classSectionId) {
        return studentRepository.findByInstituteClassSectionId(classSectionId);
    }

    public List<Student> findByInstituteId(Integer instituteId) {
        return studentRepository.findByInstituteId(instituteId);
    }

    public Page<Student> findAll(Specification<Student> specification, Pageable pageable) {
        return studentRepository.findAll(specification, pageable);
    }

    @Override
    public List<Student> findAll(Specification<Student> specification, Sort sort) {
        return studentRepository.findAll(specification, sort);
    }

    @Override
    public Student newStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

}
