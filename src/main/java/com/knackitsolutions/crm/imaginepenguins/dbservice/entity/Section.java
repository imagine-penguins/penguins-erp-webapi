package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    @Column(name = "section_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;

    @Column(name = "section_name")
    String sectionName;

    @Column(name = "institute_type", nullable = false)
    InstituteType instituteType;

    @OneToMany(mappedBy = "section")
    Set<InstituteClassSection> instituteClassSectionTeachers;

    public Section() {
    }

    public Section(Long id, String sectionName, InstituteType instituteType) {
        Id = id;
        this.sectionName = sectionName;
        this.instituteType = instituteType;
    }

    public Section(Long id, String sectionName, InstituteType instituteType, Set<InstituteClassSection> instituteClassSectionTeachers) {
        Id = id;
        this.sectionName = sectionName;
        this.instituteType = instituteType;
        this.instituteClassSectionTeachers = instituteClassSectionTeachers;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSection(String sectionName) {
        this.sectionName = sectionName;
    }

    public InstituteType getInstituteType() {
        return instituteType;
    }

    public void setInstituteType(InstituteType instituteType) {
        this.instituteType = instituteType;
    }

    public Set<InstituteClassSection> getInstituteClassSectionTeachers() {
        return instituteClassSectionTeachers;
    }

    public void setInstituteClassSectionTeachers(Set<InstituteClassSection> instituteClassSectionTeachers) {
        this.instituteClassSectionTeachers = instituteClassSectionTeachers;
    }
}
