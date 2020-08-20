package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "institute_class")
public class InstituteClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "institute_class_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "institute_id")
    Institute institute;

    @ManyToOne
    @JoinColumn(name = "class_id")
    Class classs;

    @OneToMany(mappedBy = "instituteClass")
    Set<ClassSubject> classSubject;

    public InstituteClass() {
    }

    public InstituteClass(Long id, Institute institute, Class classs, Set<ClassSubject> classSubject) {
        this.id = id;
        this.institute = institute;
        this.classs = classs;
        this.classSubject = classSubject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Class getClasss() {
        return classs;
    }

    public void setClasss(Class classs) {
        this.classs = classs;
    }

    public Set<ClassSubject> getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(Set<ClassSubject> classSubject) {
        this.classSubject = classSubject;
    }
}
