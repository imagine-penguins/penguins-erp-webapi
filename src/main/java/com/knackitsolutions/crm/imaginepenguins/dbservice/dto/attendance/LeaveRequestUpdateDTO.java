package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LeaveRequestUpdateDTO {
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private LocalDateTime endDate;
    private LeaveType leaveType;
    private String leaveReason;
    private String rejectedReason;
    private LeaveRequestStatus status;
    private Long approvedByUserId;
    private Long approvesId;
}
