package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "institute_class")
public class InstituteClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "institute_class_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classs;

    @OneToMany(mappedBy = "instituteClass")
    private Set<InstituteClassSection> classSections;

    public InstituteClass() {
    }

    public InstituteClass(Long id, Institute institute, Class classs) {
        this.id = id;
        this.institute = institute;
        this.classs = classs;
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

//    public Set<InstituteClassSectionSubject> getClassSubject() {
//        return classSubject;
//    }
//
//    public void setClassSubject(Set<InstituteClassSectionSubject> classSubject) {
//        this.classSubject = classSubject;
//    }

    public Set<InstituteClassSection> getClassSections() {
        return classSections;
    }

    public void setClassSections(Set<InstituteClassSection> classSections) {
        this.classSections = classSections;
    }

    @Override
    public String toString() {
        return "InstituteClass{" +
                "id=" + id +
                ", institute=" + institute +
                ", classs=" + classs +
                '}';
    }
}
