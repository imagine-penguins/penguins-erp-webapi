package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentAttendanceKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "attendance_id")
    private Long attendanceId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    //    public void setAttendanceId(Long attendanceId) {
//        this.attendanceId = attendanceId;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentAttendanceKey)) return false;
        StudentAttendanceKey that = (StudentAttendanceKey) o;
        return getStudentId().equals(that.getStudentId()) &&
                getAttendanceId().equals(that.getAttendanceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudentId(), getAttendanceId());
    }

    @Override
    public String toString() {
        return "StudentAttendanceKey{" +
                "studentId=" + studentId +
                ", attendanceId=" + attendanceId +
                '}';
    }


}
