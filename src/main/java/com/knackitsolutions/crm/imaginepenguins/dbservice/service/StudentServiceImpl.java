package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Parent;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.StudentNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.StudentAttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.Date;
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

    public Student newStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

/*

    @Override
    public List<AttendanceRequestDTO> loadStudentsWithClassSubjectId(Long classSectionSubjectId) {
        return null;
    }
*/

    @Override
    public List<Student> loadStudentWithClassSectionId(Long classSectionId) {
        return studentRepository.findByInstituteClassSectionId(classSectionId);
    }

    public List<Student> findByInstituteId(Integer instituteId) {
        return studentRepository.findByInstituteId(instituteId);
    }

    public List<Student> listStudentsWith(Integer instituteId, Optional<Boolean> active, Pageable pageable) {
        Specification<Student> specification = studentsByInstituteId(instituteId);
        active
                .map(StudentServiceImpl::studentsByActive)
                .ifPresent(specification::and);
        return studentRepository.findAll(specification, pageable).toList();
    }

    public List<Student> listStudentsWith(Integer instituteId, Optional<Boolean> active) {
        log.debug("listStudentsWith: {}, {}", instituteId, active);
        Specification<Student> specification = studentsByInstituteId(instituteId);
        specification = active
                .map(StudentServiceImpl::studentsByActive)
                .map(specification::and)
                .orElse(specification);

        return studentRepository.findAll(specification);
    }

    public static Specification<Student> studentsByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> {
            Join<Parent, Institute> parentInstituteJoin = root
                    .join("instituteClassSection", JoinType.LEFT)
                    .join("instituteClass", JoinType.LEFT)
                    .join("institute", JoinType.LEFT);
            Predicate equalPredicate = criteriaBuilder.equal(parentInstituteJoin.get("id"), instituteId);
            return equalPredicate;
        };
    }

    public static Specification<Student> studentsByActive(final Boolean active) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }
}
