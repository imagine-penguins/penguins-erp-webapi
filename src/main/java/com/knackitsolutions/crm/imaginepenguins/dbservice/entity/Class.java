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
    Long Id;

    @Column(name = "class_name")
    String className;

    @Column(name = "institute_type", nullable = false)
    InstituteType instituteType;

    @OneToMany(mappedBy = "classs")
    private Set<InstituteClass> instituteClasses = new HashSet<>();


    public Class(Long id, String className, InstituteType instituteType) {
        Id = id;
        this.className = className;
        this.instituteType = instituteType;
    }

    public Class(Long id, String className, InstituteType instituteType, Set<InstituteClass> instituteClasses) {
        Id = id;
        this.className = className;
        this.instituteType = instituteType;
        this.instituteClasses = instituteClasses;
    }

    public Class() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
                "Id=" + Id +
                ", className='" + className + '\'' +
                ", instituteType=" + instituteType +
                ", institutes=" + instituteClasses +
                '}';
    }

    public void addInstituteClass(InstituteClass instituteClass) {
        instituteClasses.add(instituteClass);
        instituteClass.setClasss(this);
    }
}
