package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student one(Long id);

    List<Student> all();

    List<StudentInfo> loadClassStudents(Long id);

    Optional<StudentAttendance> saveAttendance(StudentAttendance studentAttendance);

    List<StudentAttendance> saveAttendance(List<StudentAttendance> studentAttendances);

}
