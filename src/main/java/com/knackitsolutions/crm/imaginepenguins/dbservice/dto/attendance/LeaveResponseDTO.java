package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaveResponseDTO extends RepresentationModel<LeaveResponseDTO> {

    private Long id;
    private Long userId;
    private Long approvesId;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date endDate;

    private Date appliedOn;
    private LeaveType leaveType;

    private String leaveReason;

    private LeaveRequestStatus status;

    private String rejectedReason;

    private Long approvedByUserId;
}
