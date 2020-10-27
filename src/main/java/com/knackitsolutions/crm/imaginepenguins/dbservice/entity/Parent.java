package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "parents")
@PrimaryKeyJoinColumn(name = "parent_id")
public class Parent extends User{

    //QR Code

    private String QR;

    @OneToMany(mappedBy = "parent")
    private List<Student> students;

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
