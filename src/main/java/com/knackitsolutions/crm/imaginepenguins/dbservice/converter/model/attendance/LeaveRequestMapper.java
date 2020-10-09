package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestUpdateDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaveRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LeaveRequestMapper {

    @Autowired
    UserService userService;

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    public void dtoToEntity(LeaveRequest entity, LeaveRequestDTO dto) {
        if (entity == null || dto == null) {
            return;
        }
        entity.setLeaveReason(dto.getLeaveReason());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setLeaveType(dto.getLeaveType());

        User user = userService.findById(dto.getUserId());
        user.setLeaveRequests(entity);

        User approver = userService.findById(dto.getApproverId());
        approver.setLeaveRequestsApprover(entity);

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
        dto.setApprovedByUserId(entity.getApprovedBy().getId());
        dto.setApproverId(entity.getApprover().getId());
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
                .map(user -> user.getLeaveRequests()
                        .stream()
                        .map(this::entityToDTO).collect(Collectors.toList()))
                .forEach(dtos::addAll);

        return dtos;

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
