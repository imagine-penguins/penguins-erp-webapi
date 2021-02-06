package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_date")
    @Setter
    @Getter
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @Setter
    @Getter
    private LocalDateTime endDate;

    @Column(name = "update_dt_tm")
    @Setter
    @Getter
    private LocalDateTime updateDateTime;

    @Column(name = "leave_type")
    private LeaveType leaveType;

    @Column(name = "leave_reason")
    private String leaveReason;

    @JoinColumn(name = "approves_user_id")
    @ManyToOne(optional = false)
    private User approves;

    @Column(name = "request_status")
    private LeaveRequestStatus leaveRequestStatus;

    @JoinColumn(name = "approver_user_id")
    @ManyToOne
    private User approvedBy;

    @Column(name = "rejected_reason")
    private String rejectedReason;

    public LeaveRequest() {
        this.updateDateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public User getApproves() {
        return approves;
    }

    public void setApproves(User approves) {
        this.approves = approves;
    }

    public LeaveRequestStatus getLeaveRequestStatus() {
        return leaveRequestStatus;
    }

    public void setLeaveRequestStatus(LeaveRequestStatus leaveRequestStatus) {
        this.leaveRequestStatus = leaveRequestStatus;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", user=" + ((user != null) ? user.getId() : "null") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", updateDateTime=" + updateDateTime +
                ", leaveType=" + leaveType +
                ", leaveReason='" + leaveReason + '\'' +
                ", approves=" + ((approves != null) ? approves.getId() : "null") +
                ", leaveRequestStatus=" + leaveRequestStatus +
                ", approvedBy=" + ((approvedBy != null) ? approvedBy.getId() : "null") +
                ", rejectedReason='" + rejectedReason + '\'' +
                '}';
    }
}
