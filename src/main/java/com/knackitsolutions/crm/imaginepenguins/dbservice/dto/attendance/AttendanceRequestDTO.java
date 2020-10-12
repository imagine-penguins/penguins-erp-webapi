package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;

import java.util.Date;
import java.util.List;

public class AttendanceRequestDTO {

    private Long supervisorId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date attendanceDate;

    private List<UserAttendanceRequestDTO> attendanceData;

    public AttendanceRequestDTO() {
    }


    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public List<UserAttendanceRequestDTO> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<UserAttendanceRequestDTO> attendanceData) {
        this.attendanceData = attendanceData;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @Override
    public String toString() {
        return "StudentAttendanceRequestDTO{" +
                ", supervisorId=" + supervisorId +
                ", attendanceDate=" + attendanceDate +
                ", attendanceData=" + attendanceData +
                '}';
    }

    public static class UserAttendanceRequestDTO {

        private Long userId;
        private AttendanceStatus status;

        public UserAttendanceRequestDTO() {
        }

        public UserAttendanceRequestDTO(Long userId, AttendanceStatus status) {
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
}
