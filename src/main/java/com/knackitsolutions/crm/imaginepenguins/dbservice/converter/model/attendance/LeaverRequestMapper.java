package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.LeaverRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeaverRequestMapper {

    @Autowired
    UserService userService;

    public void dtoToEntity(LeaveRequest entity, LeaverRequestDTO dto) {
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

    public LeaveRequest dtoToEntity(LeaverRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        LeaveRequest entity = new LeaveRequest();
        dtoToEntity(entity, dto);
        return entity;
    }
}
