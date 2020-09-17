package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.StudentNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.StudentAttendanceRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentAttendanceRepository attendanceRepository;

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

    @Override
    public List<StudentInfo> loadClassStudents(Long classSectionId) {
        return studentRepository.findAllByClassSectionId(classSectionId);
    }

    @Override
    public Optional<StudentAttendance> saveAttendance(StudentAttendance studentAttendance) {
        return Optional.ofNullable(attendanceRepository.save(studentAttendance));
    }

    @Override
    public List<StudentAttendance> saveAttendance(List<StudentAttendance> studentAttendances) {
        return attendanceRepository.saveAll(studentAttendances);
    }
}
