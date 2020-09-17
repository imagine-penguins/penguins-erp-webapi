package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "institute_class_section")
public class InstituteClassSection {

    @Id
    @Column(name = "class_section_id")
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
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "instituteClassSection")
    private Set<InstituteClassSectionSubject> instituteClassSectionSubjects = new HashSet<>();

    @OneToMany(mappedBy = "classSection")
    private Set<StudentAttendance> studentAttendances = new HashSet<>();

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

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        students.forEach(this::addStudent);
    }

    public Set<InstituteClassSectionSubject> getInstituteClassSectionSubjects() {
        return instituteClassSectionSubjects;
    }

    public void setInstituteClassSectionSubjects(Set<InstituteClassSectionSubject> instituteClassSectionSubjects) {
        instituteClassSectionSubjects.forEach(this::setInstituteClassSectionSubjects);
    }

    public void setInstituteClassSectionSubjects(InstituteClassSectionSubject instituteClassSectionSubject) {
        this.instituteClassSectionSubjects.add(instituteClassSectionSubject);
        instituteClassSectionSubject.setInstituteClassSection(this);
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.setInstituteClassSectionTeacher(this);
    }

    public Set<StudentAttendance> getStudentAttendances() {
        return studentAttendances;
    }

    public void setStudentAttendances(Set<StudentAttendance> studentAttendances) {
        studentAttendances.forEach(this::setStudentAttendances);
    }

    public void setStudentAttendances(StudentAttendance studentAttendance) {
        studentAttendances.add(studentAttendance);
        studentAttendance.setClassSection(this);

    }

    @Override
    public String toString() {
        return "InstituteClassSection{" +
                "id=" + id +
                ", instituteClass=" + instituteClass +
                ", section=" + section +
                '}';
    }
}
