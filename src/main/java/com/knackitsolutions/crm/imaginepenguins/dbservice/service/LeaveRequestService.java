package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    public final Page<LeaveRequest> findAllBySpecification(Specification<LeaveRequest> leaveRequestSpecification, Pageable pageable) {
        return leaveRequestRepository.findAll(leaveRequestSpecification, pageable);
    }

    public final List<LeaveRequest> findAllBySpecification(Specification<LeaveRequest> specification) {
        return leaveRequestRepository.findAll(specification);

    }

    public final Long count(Specification<LeaveRequest> leaveRequestSpecification) {
        return leaveRequestRepository.count(leaveRequestSpecification);
    }
}
