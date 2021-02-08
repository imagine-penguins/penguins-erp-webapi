package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    public Boolean isOnLeave(Long userId, LocalDateTime date) {
        return leaveRequestRepository.isOnLeave(userId, date, LeaveRequestStatus.APPROVED) != null && leaveRequestRepository.isOnLeave(userId, date, LeaveRequestStatus.APPROVED) != 0;
    }
    public List<LocalDateTime> isOnLeave(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<LocalDateTime> dates = new ArrayList<>();
        while (startDate.isBefore(endDate)) {
            log.debug("LastDate: {}", startDate);
            if (isOnLeave(userId, startDate)) {
                dates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }
        if (isOnLeave(userId, endDate)) {
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

    public final LeaveRequest findByUserIdAndDate(Long userId, LocalDateTime date) {
        return leaveRequestRepository.findByUserIdAndDate(userId, date);
    }

}
