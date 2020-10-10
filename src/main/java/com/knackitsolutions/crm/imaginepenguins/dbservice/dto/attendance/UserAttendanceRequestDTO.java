package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;

public class UserAttendanceRequestDTO {

    private Long userId;
    private AttendanceStatus status;

    public UserAttendanceRequestDTO() {
    }

    public UserAttendanceRequestDTO(Long userId, Long supervisorId, AttendanceStatus status) {
        this.userId = userId;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserAttendanceRequestDTO{" +
                "userId=" + userId +
                ", status=" + status +
                '}';
    }
}
