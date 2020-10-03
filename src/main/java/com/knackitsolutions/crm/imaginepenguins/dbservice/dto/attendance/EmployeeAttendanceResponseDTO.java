package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import java.util.Optional;

public class EmployeeAttendanceResponseDTO extends UserAttendanceResponseDTO {
    private Optional<Long> attendanceId;

    public Optional<Long> getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Optional<Long> attendanceId) {
        this.attendanceId = attendanceId;
    }
}
