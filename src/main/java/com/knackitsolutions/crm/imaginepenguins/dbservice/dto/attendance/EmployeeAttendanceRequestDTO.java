package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class EmployeeAttendanceRequestDTO {

    @JsonFormat(pattern = "dd-MMM-yyyy")
    private Date attendanceDate;
    private Long supervisorId;
    private Long departmentId;
    private List<UserAttendanceRequestDTO> attendanceData;

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public List<UserAttendanceRequestDTO> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<UserAttendanceRequestDTO> attendanceData) {
        this.attendanceData = attendanceData;
    }
}
