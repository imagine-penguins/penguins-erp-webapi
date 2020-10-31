package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.TeacherNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Optional;

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

    public List<Teacher> listTeachersWith(Long instituteId, Optional<Boolean> active, Pageable pageable) {
        Specification<Teacher> specification = teachersByInstituteId(instituteId);
        specification = active
                .map(TeacherServiceImpl::teachersByActive)
                .map(specification::and)
                .orElse(specification);
        return teacherRepository.findAll(specification, pageable).toList();
    }

    public static Specification<Teacher> teachersByActive(Boolean active) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }

    public static Specification<Teacher> teachersByInstituteId(Long instituteId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                                .join("institute", JoinType.LEFT)
                                .get("id")
                        , instituteId
                );
    }
}
