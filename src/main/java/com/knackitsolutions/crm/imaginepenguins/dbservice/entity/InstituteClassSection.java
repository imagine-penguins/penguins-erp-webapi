package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "institute_class_section")
public class InstituteClassSection {

    @Id
    @Column(name = "class_section_teacher_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "institution_class_id")
    private InstituteClass instituteClass;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "instituteClassSection")
    private Set<Student> student;

    @OneToMany(mappedBy = "instituteClassSection")
    private Set<InstituteClassSectionSubject> instituteClassSectionSubjects;

    public InstituteClassSection() {
    }

    public InstituteClassSection(Long id, InstituteClass instituteClass, Section section) {
        this.id = id;
        this.instituteClass = instituteClass;
        this.section = section;
    }

    public InstituteClassSection(Long id, InstituteClass instituteClass, Section section, Teacher teacher) {
        this(id, instituteClass, section);
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstituteClass getInstituteClass() {
        return instituteClass;
    }

    public void setInstituteClass(InstituteClass instituteClass) {
        this.instituteClass = instituteClass;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudent() {
        return student;
    }

    public void setStudent(Set<Student> student) {
        this.student = student;
    }

    public Set<InstituteClassSectionSubject> getInstituteClassSectionSubjects() {
        return instituteClassSectionSubjects;
    }

    public void setInstituteClassSectionSubjects(Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        this.instituteClassSectionSubjects = instituteClassSectionSubjects;
    }
}
