package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;

import java.util.Date;

public class StudentAttendanceRequestDTO {

    private Long studentId;
    private AttendanceStatus status;

    public StudentAttendanceRequestDTO() {
    }

    public StudentAttendanceRequestDTO(Long studentId, Long supervisorId, AttendanceStatus status) {
        this.studentId = studentId;
        this.status = status;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
