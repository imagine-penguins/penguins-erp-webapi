package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    TeacherService teacherService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    StudentService studentService;

    @Autowired
    ParentService parentService;

    @Autowired
    InstituteService instituteService;

//    public Integer totalSchool(){
//        return instituteService.findAll();
//    }
//
//    public Integer totalStudents(Integer instituteId){
//        return instituteService.findAllStudents(instituteId);
//    }
//
//    public Integer totalTeachers(Integer instituteId) {
//        return instituteService.findAllTeachers(instituteId);
//    }
//
//    public Integer totalEmployees(Integer instituteId){
//        return instituteService.findAllEmployees(instituteId);
//    }
//
//    public Integer totalParents(Integer instituteId){
//        return instituteService.findAllParents(instituteId);
//    }
}
