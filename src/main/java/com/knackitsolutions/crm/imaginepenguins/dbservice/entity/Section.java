package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    @Column(name = "section_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "section_name")
    String sectionName;

    @Column(name = "institute_type", nullable = false)
    InstituteType instituteType;

    @OneToMany(mappedBy = "section")
    Set<InstituteClassSection> instituteClassSections = new HashSet<>();

    public Section() {
    }

    public Section(Long id, String sectionName, InstituteType instituteType) {
        this.id = id;
        this.sectionName = sectionName;
        this.instituteType = instituteType;
    }

    public Section(Long id, String sectionName, InstituteType instituteType, Set<InstituteClassSection> instituteClassSections) {
        this.id = id;
        this.sectionName = sectionName;
        this.instituteType = instituteType;
        this.instituteClassSections = instituteClassSections;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<InstituteClassSection> getInstituteClassSections() {
        return instituteClassSections;
    }

    public void setInstituteClassSections(Set<InstituteClassSection> instituteClassSections) {
        instituteClassSections.forEach(this::addInstituteClassSection);
    }

    public void addInstituteClassSection(InstituteClassSection instituteClassSection) {
        this.instituteClassSections.add(instituteClassSection);
        instituteClassSection.setSection(this);
    }

    @Override
    public String toString() {
        return "Section{" +
                "Id=" + id +
                ", sectionName='" + sectionName + '\'' +
                ", instituteType=" + instituteType +
                '}';
    }
}
