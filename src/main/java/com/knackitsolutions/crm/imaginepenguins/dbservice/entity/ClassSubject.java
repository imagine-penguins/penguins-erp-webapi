package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "class_subjects")
public class ClassSubject {

    @Id
    @Column(name = "class_subject_id")
    Long Id;

    @ManyToOne
    @JoinColumn(name = "institute_class_id")
    InstituteClass instituteClass;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    Subject subject;

    public ClassSubject() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public InstituteClass getInstituteClass() {
        return instituteClass;
    }

    public void setInstituteClass(InstituteClass instituteClass) {
        this.instituteClass = instituteClass;
    }

    public Subject getSubejct() {
        return subject;
    }

    public void setSubejct(Subject subejct) {
        this.subject = subejct;
    }
}
