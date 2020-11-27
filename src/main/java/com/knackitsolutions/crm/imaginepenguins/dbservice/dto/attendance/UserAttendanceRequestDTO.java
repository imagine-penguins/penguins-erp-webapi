package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAttendanceRequestDTO {
    private Long userId;
    private AttendanceStatus status;
}
