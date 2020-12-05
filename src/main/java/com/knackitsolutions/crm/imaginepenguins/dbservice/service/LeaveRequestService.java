package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    public Boolean isOnLeave(Long userId, Date date) {
        if (leaveRequestRepository.isOnLeave(userId, date) != null) {
            return true;
        }
        return false;
    }
}
