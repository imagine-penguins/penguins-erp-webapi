package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

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
    @JoinColumn(name = "parent_id")
    Parent parent;

    public Student() {
    }

    public Student(InstituteClassSection instituteClassSection, Parent parent) {
        this.instituteClassSection = instituteClassSection;
        this.parent = parent;
    }

    public Student(Long id, String username, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, InstituteClassSection instituteClassSection, Parent parent) {
        super(id, username, userType, isAdmin, isSuperAdmin);
        this.instituteClassSection = instituteClassSection;
        this.parent = parent;
    }

    public Student(Long id, String username, String password, UserType userType, Boolean isAdmin, Boolean isSuperAdmin, UserProfile userProfile, InstituteClassSection instituteClassSection, Parent parent) {
        super(id, username, password, userType, isAdmin, isSuperAdmin, userProfile);
        this.instituteClassSection = instituteClassSection;
        this.parent = parent;
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

    @Override
    public String toString() {

        return super.toString() +
                "Student{" +
                "instituteClassSection=" + instituteClassSection +
                ", parent=" + parent +
                '}';
    }
}
