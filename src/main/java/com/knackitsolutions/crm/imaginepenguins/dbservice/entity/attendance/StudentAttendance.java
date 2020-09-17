package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;

import javax.persistence.*;

@Entity
@Table(name = "student_attendance")
public class StudentAttendance {

    @EmbeddedId
    StudentAttendanceKey studentAttendanceKey;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @MapsId("studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    @MapsId("attendanceId")
    private Attendance attendance;

    @ManyToOne
    @JoinColumn(name = "subject_class_id")
    private InstituteClassSectionSubject instituteClassSectionSubject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private InstituteClassSection classSection;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public StudentAttendanceKey getStudentAttendanceKey() {
        return studentAttendanceKey;
    }

    public void setStudentAttendanceKey(StudentAttendanceKey studentAttendanceKey) {
        this.studentAttendanceKey = studentAttendanceKey;
    }

    public InstituteClassSectionSubject getInstituteClassSectionSubject() {
        return instituteClassSectionSubject;
    }

    public void setInstituteClassSectionSubject(InstituteClassSectionSubject instituteClassSectionSubject) {
        this.instituteClassSectionSubject = instituteClassSectionSubject;
    }

    public InstituteClassSection getClassSection() {
        return classSection;
    }

    public void setClassSection(InstituteClassSection classSection) {
        this.classSection = classSection;
    }


}
