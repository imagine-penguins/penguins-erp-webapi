package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    public Boolean leaves(Long userId, Date date) {
        return leaveRequestRepository.isOnLeave(userId, date) != null && leaveRequestRepository.isOnLeave(userId, date) != 0;
    }
    public List<Date> leaves(Long userId, Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        while (startDate.before(endDate)) {
            log.debug("LastDate: {}", startDate);
            if (leaves(userId, startDate)) {
                dates.add(startDate);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }
        if (leaves(userId, endDate)) {
            dates.add(endDate);
        }
        return dates;
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

    public final LeaveRequest findByUserIdAndDate(Long userId, Date date) {
        return leaveRequestRepository.findByUserIdAndDate(userId, date);
    }
}
