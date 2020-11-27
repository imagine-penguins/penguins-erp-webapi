package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LeaveRequestDTO {

    private String title;
    private Long approvesId;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date endDate;

    private LeaveType leaveType;

    private String leaveReason;

}
