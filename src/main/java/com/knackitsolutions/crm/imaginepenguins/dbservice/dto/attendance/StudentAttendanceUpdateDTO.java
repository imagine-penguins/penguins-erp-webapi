package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;

public class StudentAttendanceUpdateDTO {
    private Long studentAttendanceId;
    private AttendanceStatus attendanceStatus;

    public StudentAttendanceUpdateDTO(Long studentAttendanceId, AttendanceStatus attendanceStatus) {
        this.studentAttendanceId = studentAttendanceId;
        this.attendanceStatus = attendanceStatus;
    }

    public Long getStudentAttendanceId() {
        return studentAttendanceId;
    }

    public void setStudentAttendanceId(Long studentAttendanceId) {
        this.studentAttendanceId = studentAttendanceId;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
