package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teacher")
public class Teacher{

    @Id
    @Column(name = "teacher_id")
    Long id;

    @OneToMany(mappedBy = "teacher")
    Set<TeacherSubject> teacherSubjects;

    @OneToMany(mappedBy = "teacher")
    Set<InstituteClassSectionTeacher> instituteClassSectionTeachers;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
            @JoinColumn(name = "teacher_id", referencedColumnName = "employee_id")
    Employee employee;

    public Teacher() {
    }

    public Teacher(Long id, Set<TeacherSubject> teacherSubjects) {
        this.id = id;
        this.teacherSubjects = teacherSubjects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<TeacherSubject> getTeacherSubjects() {
        return teacherSubjects;
    }

    public void setTeacherSubjects(Set<TeacherSubject> teacherSubjects) {
        this.teacherSubjects = teacherSubjects;
    }

    public Set<InstituteClassSectionTeacher> getInstituteClassSectionTeachers() {
        return instituteClassSectionTeachers;
    }

    public void setInstituteClassSectionTeachers(Set<InstituteClassSectionTeacher> instituteClassSectionTeachers) {
        this.instituteClassSectionTeachers = instituteClassSectionTeachers;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
