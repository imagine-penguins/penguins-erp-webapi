package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student one(Long id);

    List<Student> all();

    List<StudentAttendanceResponseDTO> loadStudentResponseDTOWithClassSectionId(Long classSectionId);

    List<Student> loadStudentWithClassSectionId(Long classSectionId);

    List<StudentAttendanceRequestDTO> loadStudentsWithClassSubjectId(Long classSectionSubjectId);

    Optional<StudentAttendance> saveAttendance(StudentAttendance studentAttendance);

    List<StudentAttendance> saveAttendance(List<StudentAttendance> studentAttendances);

    List<StudentAttendance> getStudentAttendancesByClassId(Long classId);

    List<StudentAttendance> getStudentAttendancesByClassId(Long classId
            , Optional<Date> updateTimeStart, Optional<Date> updateTimeEnd);

    List<StudentAttendance> getStudentAttendancesByStudentId(Long studentId);

    List<StudentAttendance> getStudentAttendancesByStudentId(Long studentId
            , Optional<Date> updateTimeStart, Optional<Date> updateTimeEnd);

    StudentAttendance getStudentAttendanceById(StudentAttendanceKey studentAttendanceKey);

    List<StudentAttendance> getStudentAttendanceByClassSubjectId(Long subjectClassId);
    List<StudentAttendance> getStudentAttendanceByClassSubjectId(Long subjectClassId, Date updateTimeStart, Date updateTimeEnd);

}
