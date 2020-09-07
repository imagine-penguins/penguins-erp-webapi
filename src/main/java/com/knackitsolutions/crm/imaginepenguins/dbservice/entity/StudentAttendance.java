package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

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
}
