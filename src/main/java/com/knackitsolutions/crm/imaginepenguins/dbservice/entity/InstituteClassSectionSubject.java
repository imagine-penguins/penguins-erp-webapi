package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "class_subjects")
public class InstituteClassSectionSubject {

    @Id
    @Column(name = "class_subject_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToMany(mappedBy = "instituteClassSectionSubject")
    private Set<StudentAttendance> studentAttendances = new HashSet<>();

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

    public Set<StudentAttendance> getStudentAttendances() {
        return studentAttendances;
    }

    public void setStudentAttendances(Set<StudentAttendance> studentAttendances) {
        studentAttendances.forEach(this::setStudentAttendance);
    }

    public void setStudentAttendance(StudentAttendance studentAttendance) {
        this.studentAttendances.add(studentAttendance);
        studentAttendance.setInstituteClassSectionSubject(this);
    }

    @Override
    public String toString() {
        return "InstituteClassSectionSubject{" +
                "id=" + id +
                ", instituteClassSection=" + instituteClassSection +
                ", subject=" + subject +
                '}';
    }

}
