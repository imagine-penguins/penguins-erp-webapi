package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;

import java.util.Date;

public class LeaveResponseDTO {

    private Long id;
    private Long userId;
    private Long approverId;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date endDate;

    private LeaveType leaveType;

    private String leaveReason;

    private LeaveRequestStatus status;

    private String rejectedReason;

    private Long approvedByUserId;

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

    public LeaveRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveRequestStatus status) {
        this.status = status;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public Long getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(Long approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LeaveResponseDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", approverId=" + approverId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", leaveType=" + leaveType +
                ", leaveReason='" + leaveReason + '\'' +
                ", status=" + status +
                ", rejectedReason='" + rejectedReason + '\'' +
                ", approvedByUserId=" + approvedByUserId +
                '}';
    }
}
