package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teacher")
@PrimaryKeyJoinColumn(name = "teacher_id")
public class Teacher extends Employee{
    @OneToMany(mappedBy = "teacher")
    private Set<TeacherSubject> teacherSubjects = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<InstituteClassSection> instituteClassSections = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<InstituteClassSectionSubject> instituteClassSectionSubjects = new HashSet<>();

    public Teacher() {
    }

    public Teacher(Long id, String username, String password, UserType userType, Boolean isAdmin, Boolean isSuperAdmin
            , UserProfile userProfile, EmployeeType employeeType, String designation) {
        super(id, username, password, userType, isAdmin, isSuperAdmin, userProfile, employeeType, designation);
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

    public void setInstituteClassSections(List<InstituteClassSection> instituteClassSections) {
        instituteClassSections.forEach(this::setInstituteClassSections);
    }

    public void setInstituteClassSections(InstituteClassSection instituteClassSection) {
        instituteClassSections.add(instituteClassSection);
        instituteClassSection.setTeacher(this);
    }

    public Set<InstituteClassSectionSubject> getInstituteClassSectionSubjects() {
        return instituteClassSectionSubjects;
    }

    public void setInstituteClassSectionSubjects(Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        instituteClassSectionSubjects.forEach(this::setInstituteClassSectionSubjects);

    }

    public void setInstituteClassSectionSubjects(InstituteClassSectionSubject instituteClassSectionSubject) {
        instituteClassSectionSubjects.add(instituteClassSectionSubject);
        instituteClassSectionSubject.setTeacher(this);
    }

    @Override
    public String toString() {

        return super.toString() +
                "Teacher{" +
                "teacherSubjects=" + teacherSubjects +
                ", instituteClassSections=" + instituteClassSections +
                ", instituteClassSectionSubjects=" + instituteClassSectionSubjects +
                '}';
    }
}
