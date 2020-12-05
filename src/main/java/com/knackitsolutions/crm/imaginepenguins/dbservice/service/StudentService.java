package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    Long count(Specification<Student> specification);


    Student one(Long id);

    List<Student> all();

    List<Student> loadStudentWithClassSectionId(Long classSectionId);

//    List<Student> listStudentsWith(Integer instituteId, Optional<Boolean> active, Pageable pageable);

    List<Student> listStudentsWith(Integer instituteId, Optional<Boolean> active);

    Page<Student> findAll(Specification<Student> specification, Pageable pageable);

    List<Student> findAll(Specification<Student> specification, Sort sort);
}
