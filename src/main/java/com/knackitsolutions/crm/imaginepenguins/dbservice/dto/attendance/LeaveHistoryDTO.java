package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class LeaveHistoryDTO extends RepresentationModel<LeaveHistoryDTO> {

    List<LeaveResponseDTO> leaveResponseDTO;
    List<GraphData> graphData;

    public List<LeaveResponseDTO> getLeaveResponseDTO() {
        return leaveResponseDTO;
    }

    public void setLeaveResponseDTO(List<LeaveResponseDTO> leaveResponseDTO) {
        this.leaveResponseDTO = leaveResponseDTO;
    }

    public List<GraphData> getGraphData() {
        return graphData;
    }

    public void setGraphData(List<GraphData> graphData) {
        this.graphData = graphData;
    }

    public static class GraphData {
        private String month;
        private Long leaveCount;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Long getLeaveCount() {
            return leaveCount;
        }

        public void setLeaveCount(Long leaveCount) {
            this.leaveCount = leaveCount;
        }
    }
}
