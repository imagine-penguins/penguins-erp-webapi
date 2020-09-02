package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teacher")
@PrimaryKeyJoinColumn(name = "teacher_id")
public class Teacher extends Employee{

//    @Id
//    @Column(name = "teacher_id")
//    Long id;

    @OneToMany(mappedBy = "teacher")
    private Set<TeacherSubject> teacherSubjects = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<InstituteClassSection> instituteClassSections = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<InstituteClassSectionSubject> instituteClassSectionSubjects = new HashSet<>();
//    @OneToOne(cascade = CascadeType.ALL)
//    @MapsId
//            @JoinColumn(name = "teacher_id", referencedColumnName = "employee_id")
//    Employee employee;


    public Teacher() {
    }

    public Teacher(Set<TeacherSubject> teacherSubjects, Set<InstituteClassSection> instituteClassSections, Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        this.teacherSubjects = teacherSubjects;
        this.instituteClassSections = instituteClassSections;
        this.instituteClassSectionSubjects = instituteClassSectionSubjects;
    }

    public Teacher(Long id, String username, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, EmployeeType employeeType, String designation, Set<TeacherSubject> teacherSubjects, Set<InstituteClassSection> instituteClassSections, Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        super(id, username, userType, isAdmin, isSuperAdmin, employeeType, designation);
        this.teacherSubjects = teacherSubjects;
        this.instituteClassSections = instituteClassSections;
        this.instituteClassSectionSubjects = instituteClassSectionSubjects;
    }

    public Teacher(EmployeeType employeeType, String designation, Institute institute, Employee manager, Set<Employee> subordinates, Set<TeacherSubject> teacherSubjects, Set<InstituteClassSection> instituteClassSections, Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        super(employeeType, designation, institute, manager, subordinates);
        this.teacherSubjects = teacherSubjects;
        this.instituteClassSections = instituteClassSections;
        this.instituteClassSectionSubjects = instituteClassSectionSubjects;
    }

    public Teacher(Long id, String username, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, EmployeeType employeeType, String designation, Institute institute, Employee manager, Set<Employee> subordinates, Set<TeacherSubject> teacherSubjects, Set<InstituteClassSection> instituteClassSections, Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        super(id, username, userType, isAdmin, isSuperAdmin, employeeType, designation, institute, manager, subordinates);
        this.teacherSubjects = teacherSubjects;
        this.instituteClassSections = instituteClassSections;
        this.instituteClassSectionSubjects = instituteClassSectionSubjects;
    }

    public Teacher(Long id, String username, String password, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, UserProfile userProfile, EmployeeType employeeType, String designation) {
        super(id, username, password, userType, isAdmin, isSuperAdmin, userProfile, employeeType, designation);
        this.teacherSubjects = teacherSubjects;
        this.instituteClassSections = instituteClassSections;
        this.instituteClassSectionSubjects = instituteClassSectionSubjects;
    }
    public Teacher(Long id, String username, String password, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, UserProfile userProfile, EmployeeType employeeType, String designation, Institute institute, Employee manager, Set<Employee> subordinates, Set<TeacherSubject> teacherSubjects, Set<InstituteClassSection> instituteClassSections, Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        super(id, username, password, userType, isAdmin, isSuperAdmin, userProfile, employeeType, designation, institute, manager, subordinates);
        this.teacherSubjects = teacherSubjects;
        this.instituteClassSections = instituteClassSections;
        this.instituteClassSectionSubjects = instituteClassSectionSubjects;
    }

    public Set<TeacherSubject> getTeacherSubjects() {
        return teacherSubjects;
    }

    public void setTeacherSubjects(TeacherSubject teacherSubject) {
        this.teacherSubjects.add(teacherSubject);
    }

    public Set<InstituteClassSection> getInstituteClassSections() {
        return instituteClassSections;
    }

    public void setInstituteClassSections(InstituteClassSection instituteClassSection) {
        instituteClassSections.add(instituteClassSection);
    }

    public Set<InstituteClassSectionSubject> getInstituteClassSectionSubjects() {
        return instituteClassSectionSubjects;
    }

    public void setInstituteClassSectionSubjects(InstituteClassSectionSubject instituteClassSectionSubject) {
        instituteClassSectionSubjects.add(instituteClassSectionSubject);
    }
}
