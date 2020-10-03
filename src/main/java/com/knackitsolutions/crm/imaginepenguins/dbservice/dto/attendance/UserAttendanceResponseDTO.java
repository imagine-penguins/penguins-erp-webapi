package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Optional;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = StudentAttendanceResponseDTO.class, name = "student"),
                @JsonSubTypes.Type(value = EmployeeAttendanceResponseDTO.class, name = "employee")
})
public class UserAttendanceResponseDTO extends RepresentationModel<UserAttendanceResponseDTO> {
    private Long userId;
    private String firstName;
    private String lastName;
    private String profilePic;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date attendanceDate;

    private Optional<AttendanceStatus> status;

    public UserAttendanceResponseDTO() {
    }

    public UserAttendanceResponseDTO(Long userId, String firstName, String lastName, String profilePic) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePic = profilePic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Optional<AttendanceStatus> getStatus() {
        return status;
    }

    public void setStatus(Optional<AttendanceStatus> status) {
        this.status = status;
    }
}
