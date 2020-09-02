package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "class_subjects")
public class InstituteClassSectionSubject {

    @Id
    @Column(name = "class_subject_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "institute_class_id")
    private InstituteClassSection instituteClassSection;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public InstituteClassSectionSubject() {
    }

    public InstituteClassSectionSubject(Long id, InstituteClassSection instituteClassSection, Subject subject) {
        this.id = id;
        this.instituteClassSection = instituteClassSection;
        this.subject = subject;
    }
    public InstituteClassSectionSubject(Long id, InstituteClassSection instituteClassSection, Subject subject, Teacher teacher) {
        this.id = id;
        this.instituteClassSection = instituteClassSection;
        this.subject = subject;
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstituteClassSection getInstituteClass() {
        return instituteClassSection;
    }

    public void setInstituteClass(InstituteClassSection instituteClassSection) {
        this.instituteClassSection = instituteClassSection;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public InstituteClassSection getInstituteClassSection() {
        return instituteClassSection;
    }

    public void setInstituteClassSection(InstituteClassSection instituteClassSection) {
        this.instituteClassSection = instituteClassSection;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

}
