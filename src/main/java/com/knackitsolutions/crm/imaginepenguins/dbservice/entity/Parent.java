package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "parents")
public class Parent{

    @Id
    @Column(name = "parent_id")
    Long id;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "parent_id", referencedColumnName = "user_id")
    User user;

    //QR Code

    String QR;

    @OneToMany(mappedBy = "parent")
    List<Student> students;

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

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Parent() {
    }
}
