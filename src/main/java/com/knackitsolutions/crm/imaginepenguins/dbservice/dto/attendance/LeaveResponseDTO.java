package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaveResponseDTO extends RepresentationModel<LeaveResponseDTO> {

    private Long id;

    private Long userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String profilePic;
    private String passportPic;
    private Long approvesId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime appliedOn;
    private LeaveType leaveType;
    private String leaveReason;
    private LeaveRequestStatus status;
    private String rejectedReason;
    private Long approvedByUserId;
}
