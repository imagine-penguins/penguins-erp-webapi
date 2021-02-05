package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAttendanceResponseDTO extends RepresentationModel<UserAttendanceResponseDTO> {
    private Long userId;
    private String firstName;
    private String lastName;
    private String profilePic;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date attendanceDate;

    private Optional<AttendanceStatus> status;
    private Long leaveRequestId;
    private String employeeId;

    private String rollNumber;

    private Optional<Long> attendanceId;

}
