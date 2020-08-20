package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student{

    @Id
    @Column(name = "student_id")
    Long id;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "class_section_id", nullable = false)
    InstituteClassSectionTeacher instituteClassSectionTeacher;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    Parent parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, instituteClassSectionTeacher);
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InstituteClassSectionTeacher getInstituteClassSectionTeacher() {
        return instituteClassSectionTeacher;
    }

    public void setInstituteClassSectionTeacher(InstituteClassSectionTeacher instituteClassSectionTeacher) {
        this.instituteClassSectionTeacher = instituteClassSectionTeacher;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
