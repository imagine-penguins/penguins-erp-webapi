package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "class")
@Table(name = "classes")
public class Class {
    @Id
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "class_name")
    String className;

    @Column(name = "institute_type", nullable = false)
    InstituteType instituteType;

    @OneToMany(mappedBy = "classs")
    private Set<InstituteClass> instituteClasses = new HashSet<>();


    public Class(Long id, String className, InstituteType instituteType) {
        this.id = id;
        this.className = className;
        this.instituteType = instituteType;
    }

    public Class(Long id, String className, InstituteType instituteType, Set<InstituteClass> instituteClasses) {
        this.id = id;
        this.className = className;
        this.instituteType = instituteType;
        this.instituteClasses = instituteClasses;
    }

    public Class() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public InstituteType getInstituteType() {
        return instituteType;
    }

    public void setInstituteType(InstituteType instituteType) {
        this.instituteType = instituteType;
    }

    public Set<InstituteClass> getInstitutes() {
        return instituteClasses;
    }

    public void setInstitutes(Set<InstituteClass> institutes) {
        this.instituteClasses.addAll(institutes);
    }

    @Override
    public String toString() {
        return "Class{" +
                "Id=" + id +
                ", className='" + className + '\'' +
                ", instituteType=" + instituteType +
                '}';
    }

    public void addInstituteClass(InstituteClass instituteClass) {
        instituteClasses.add(instituteClass);
        instituteClass.setClasss(this);
    }
}
