package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import java.util.List;

public class EmployeeAttendanceRequestDTO {
    private List<UserAttendanceRequestDTO> attendanceData;

    public List<UserAttendanceRequestDTO> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<UserAttendanceRequestDTO> attendanceData) {
        this.attendanceData = attendanceData;
    }
}
