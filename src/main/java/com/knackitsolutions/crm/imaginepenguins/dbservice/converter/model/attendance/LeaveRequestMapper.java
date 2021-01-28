package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveHistoryDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestUpdateDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

@Component
@Slf4j
public class LeaveRequestMapper {

    public void dtoToEntity(LeaveRequest entity, LeaveRequestDTO dto) {
        if (entity == null || dto == null) {
            return;
        }
        entity.setLeaveReason(dto.getLeaveReason());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setLeaveType(dto.getLeaveType());

    }

    public LeaveRequest dtoToEntity(LeaveRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        LeaveRequest entity = new LeaveRequest();
        dtoToEntity(entity, dto);
        return entity;
    }

    public LeaveResponseDTO entityToDTO(LeaveRequest entity) {
        if (entity == null) {
            return null;
        }
        LeaveResponseDTO dto = new LeaveResponseDTO();
        dto.setUserId(entity.getUser().getId());
        dto.setId(entity.getId());

        if (entity.getApprovedBy() != null)
            dto.setApprovedByUserId(entity.getApprovedBy().getId());

        dto.setApprovesId(entity.getApproves().getId());
        dto.setEndDate(entity.getEndDate());
        dto.setStartDate(entity.getStartDate());
        dto.setLeaveReason(entity.getLeaveReason());
        dto.setLeaveType(entity.getLeaveType());
        dto.setRejectedReason(entity.getRejectedReason());
        dto.setStatus(entity.getLeaveRequestStatus());
        dto.setAppliedOn(entity.getUpdateDateTime());
        User user = entity.getUser();
        dto.setFirstName(user.getUserProfile().getFirstName());
        dto.setLastName(user.getUserProfile().getLastName());
        return dto;
    }

    public void dtoToEntity(LeaveRequest entity, LeaveRequestUpdateDTO dto) {
        if (dto == null || entity == null) {
            return ;
        }
        entity.setLeaveType(dto.getLeaveType());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setLeaveReason(dto.getLeaveReason());

    }

    public LeaveHistoryDTO.GraphData getGraphDataFromMapEntry(Map.Entry<String, Integer> leaveCount) {
        log.info("Starting getGraphDataFromMapEntry method.");
        LeaveHistoryDTO.GraphData gData = new LeaveHistoryDTO.GraphData();
        gData.setPeriod(leaveCount.getKey());
        gData.setLeaveCount(leaveCount.getValue());
        log.info("Graph Data: {}", gData);
        return gData;
    }

    public Map<String, Integer> getLeaveCount(List<Date> dates, Period period) {
        log.info("Starting getLeaveCount method.");

        Map<String, Integer> leaveCount = new HashMap<>();

        dates.stream().forEach(date -> {
            String key = null;
            if (period == Period.MONTH)
                key = String.valueOf(date.toInstant().atZone(ZoneId.systemDefault()).getMonth().getValue());
            else if(period == Period.DAY)
                key = new SimpleDateFormat("dd-MM-yyyy").format(date);
            Integer count = 0;
            if (leaveCount.containsKey(key))
                count = leaveCount.get(key);
            leaveCount.put(key, count + 1);
        });
        leaveCount.entrySet()
                .stream()
                .forEach(entry -> log.info("monthly count key: {}, value:{}", entry.getKey(), entry.getValue()));
        return leaveCount;
    }

    /*public Map<Month, Integer> getMonthlyLeaveCount(List<Date> dates) {
        log.info("Starting getMonthlyLeaveCount method.");
        Map<Month, Integer> monthlyCount = new HashMap<>();
        dates.stream().forEach(date -> {
            Month month = date.toInstant().atZone(ZoneId.systemDefault()).getMonth();
            Integer count = 0;
            if (monthlyCount.containsKey(month))
                count = monthlyCount.get(month);
            monthlyCount.put(month, count + 1);
        });
        monthlyCount.entrySet()
                .stream()
                .forEach(entry -> log.info("monthly count key: {}, value:{}", entry.getKey(), entry.getValue()));
        return monthlyCount;
    }*/

    public List<Date> getLeavesDates(LeaveRequest leaveRequest) {
        log.info("Starting getLeavesDates method.");
        List<Date> dates = new ArrayList<>();
        Date startDate = leaveRequest.getStartDate();
        while (!leaveRequest.getEndDate().before(startDate)) {
            log.info("next start date: {}", startDate);
            dates.add(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }
        return dates;
    }

}
