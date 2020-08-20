package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name ="teacher_subject")
public class TeacherSubject {

    @Id
    @Column(name = "teacher_subject_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    Subject subject;

    public TeacherSubject() {
    }

    public TeacherSubject(Long id, Teacher teacher, Subject subject) {
        this.id = id;
        this.teacher = teacher;
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherSubject)) return false;
        TeacherSubject that = (TeacherSubject) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTeacher(), that.getTeacher()) &&
                Objects.equals(getSubject(), that.getSubject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTeacher(), getSubject());
    }
}
