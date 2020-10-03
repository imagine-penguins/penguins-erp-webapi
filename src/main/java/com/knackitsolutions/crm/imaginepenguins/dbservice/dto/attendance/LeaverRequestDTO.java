package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;

import java.util.Date;

public class LeaverRequestDTO {

    private Long userId;
    private Long approverId;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date endDate;

    private LeaveType leaveType;

    private String leaveReason;

    public LeaverRequestDTO() {
    }

    public LeaverRequestDTO(Long userId, Long approverId, Date startDate, Date endDate
            , LeaveType leaveType, String leaveReason) {
        this.userId = userId;
        this.approverId = approverId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.leaveReason = leaveReason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }
}
