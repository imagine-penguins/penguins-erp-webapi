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

    @Column(name = "section")
    String section;

    @Column(name = "institute_type", nullable = false)
    InstituteType instituteType;

    @OneToMany(mappedBy = "section")
    Set<InstituteClassSectionTeacher> instituteClassSectionTeachers;

}
