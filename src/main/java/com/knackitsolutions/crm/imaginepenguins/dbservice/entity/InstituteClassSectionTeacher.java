package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "institute_class_section_teacher")
public class InstituteClassSectionTeacher {

    @Id
    @Column(name = "class_section_teacher_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "institution_class_id")
    InstituteClass instituteClass;

    @ManyToOne
    @JoinColumn(name = "section_id")
    Section section;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    Teacher teacher;

    @OneToMany(mappedBy = "instituteClassSectionTeacher")
    Set<Student> student;
}
