package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveHistoryDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestUpdateDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        dto.setApprovesId(entity.getApprover().getId());
        dto.setEndDate(entity.getEndDate());
        dto.setStartDate(entity.getStartDate());
        dto.setLeaveReason(entity.getLeaveReason());
        dto.setLeaveType(entity.getLeaveType());
        dto.setRejectedReason(entity.getRejectedReason());
        dto.setStatus(entity.getLeaveRequestStatus());
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

    public List<LeaveResponseDTO> leaveResponseDTOFromUser(List<User> users) {

        List<LeaveResponseDTO> dtos = new ArrayList<>();
        users
                .stream()
                .flatMap(user -> user.getLeaveRequests()
                        .stream()
                        .map(this::entityToDTO))
                .forEach(dtos::add);

        return dtos;

    }

    public void updateGraphDataList(List<LeaveHistoryDTO.GraphData> graphDataList
            , List<LeaveHistoryDTO.GraphData> graphData) {
        log.info("Creating list of graph data");
        for (LeaveHistoryDTO.GraphData graphData1 : graphDataList) {
            log.debug("Graph Data 1: {}", graphData1);
            LeaveHistoryDTO.GraphData graphData2 = null;
            if (!graphData.contains(graphData1)) {
                graphData2 = new LeaveHistoryDTO.GraphData();
            }else{
                graphData2 = graphData
                        .stream()
                        .filter(g -> graphData1.equals(g))
                        .findFirst().orElseThrow(() -> new RuntimeException("Month name is invalid: {}" + graphData1.getMonth()));
            }
            graphData2.setMonth(graphData1.getMonth());
            graphData2.setLeaveCount(graphData1.getLeaveCount());
            log.debug("Graph Data 2: {}", graphData2);
            graphData.add(graphData2);
        }
    }

    public LeaveHistoryDTO.GraphData getGraphDataFromMapEntry(Map.Entry<Month, Integer> monthlyCount) {
        log.info("Starting getGraphDataFromMapEntry method.");
        LeaveHistoryDTO.GraphData gData = new LeaveHistoryDTO.GraphData();
        gData.setMonth(monthlyCount.getKey().toString());
        gData.setLeaveCount(monthlyCount.getValue());
        log.info("Graph Data: {}", gData);
        return gData;
    }

    public List<Date> getUserLeavesDates(User user){
        log.info("Starting getMonthlyLeaveCountForUser method.");
        List<LeaveRequest> leaveRequests = user.getLeaveRequests();
        List<Date> dates = new ArrayList<>();
        leaveRequests
                .stream()
                .map(this::getLeavesDates)
                .forEach(dates::addAll);
        return dates;
    }

    public Map<Month, Integer> getMonthlyLeaveCount(List<Date> dates) {
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
    }

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

//    public List<LeaveHistoryDTO.GraphData> graphData(List<LeaveResponseDTO> leaveResponseDTOS) {
//
//    }

//    public List<Integer> getMonthsLeaveCount(List<LeaveResponseDTO> leaveResponseDTOS){
//        leaveResponseDTOS
//                .stream()
//                .map(leaveResponseDTO -> )
//    }
}
