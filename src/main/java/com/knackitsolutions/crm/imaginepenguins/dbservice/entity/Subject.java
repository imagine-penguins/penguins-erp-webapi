package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @Column(name = "section_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "subject")
//    @Column(insertable = false, updatable = false)
    private Set<InstituteClassSectionSubject> classSubjects = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    private Set<TeacherSubject> teacherSubjects = new HashSet<>();

    public Subject(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject(Long id, String name, Set<InstituteClassSectionSubject> classSubjects, Set<TeacherSubject> teacherSubjects) {
        this(id, name);
        this.classSubjects = classSubjects;
        this.teacherSubjects = teacherSubjects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<InstituteClassSectionSubject> getClassSubjects() {
        return classSubjects;
    }

    public void setClassSubjects(InstituteClassSectionSubject classSubject) {
        classSubjects.add(classSubject);
    }

    public Set<TeacherSubject> getTeacherSubjects() {
        return teacherSubjects;
    }

    public void setTeacherSubjects(TeacherSubject teacherSubject) {
        teacherSubjects.add(teacherSubject);
    }
}
