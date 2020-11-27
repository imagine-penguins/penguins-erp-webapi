package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceHistoryDTO extends RepresentationModel<AttendanceHistoryDTO> {
    private List<UserAttendanceResponseDTO> users;

    private GraphData graphData;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphData {
        private Integer presentPercent;
        private Integer absentPercent;
        private Integer leavePercent;
    }


}
