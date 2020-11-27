package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student one(Long id);

    List<Student> all();

    List<Student> loadStudentWithClassSectionId(Long classSectionId);

    List<Student> listStudentsWith(Integer instituteId, Optional<Boolean> active, Pageable pageable);

    List<Student> listStudentsWith(Integer instituteId, Optional<Boolean> active);
}
