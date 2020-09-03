package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "student_id")
public class Student extends User{

//    @Id
//    @Column(name = "student_id")
//    Long id;

//    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
//    @MapsId
//    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
//    User user;

    @ManyToOne
    @JoinColumn(name = "class_section_id", nullable = false)
    InstituteClassSection instituteClassSection;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    Parent parent;

    public Student() {
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public InstituteClassSection getInstituteClassSection() {
        return instituteClassSection;
    }

    public void setInstituteClassSectionTeacher(InstituteClassSection instituteClassSection) {
        this.instituteClassSection = instituteClassSection;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(getInstituteClassSection(), student.getInstituteClassSection()) &&
                Objects.equals(getParent(), student.getParent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getInstituteClassSection(), getParent());
    }
}
