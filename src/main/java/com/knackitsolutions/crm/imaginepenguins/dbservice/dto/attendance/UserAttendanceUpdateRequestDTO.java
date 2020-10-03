package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Optional;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StudentAttendanceUpdateRequestDTO.class, name = "student")
})
public class UserAttendanceUpdateRequestDTO {
    private Long supervisorId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date attendanceDate;
    private AttendanceStatus status;

    public UserAttendanceUpdateRequestDTO(Long supervisorId, Date attendanceDate, AttendanceStatus status) {
        this.supervisorId = supervisorId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public UserAttendanceUpdateRequestDTO() {
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserAttendanceUpdateRequestDTO{" +
                "supervisorId=" + supervisorId +
                ", attendanceDate=" + attendanceDate +
                ", status=" + status +
                '}';
    }
}
