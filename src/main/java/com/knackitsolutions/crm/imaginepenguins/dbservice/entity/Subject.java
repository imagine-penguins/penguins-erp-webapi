package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @Column(name = "section_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "subject")
//    @Column(insertable = false, updatable = false)
    private Set<ClassSubject> classSubjects;

    @OneToMany(mappedBy = "subject")
    private Set<TeacherSubject> teacherSubjects;
}
