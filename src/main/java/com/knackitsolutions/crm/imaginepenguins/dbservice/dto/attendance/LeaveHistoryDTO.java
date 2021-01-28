package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

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
        private String period;
        private Integer leaveCount;

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public Integer getLeaveCount() {
            return leaveCount;
        }

        public void setLeaveCount(Integer leaveCount) {
            this.leaveCount = leaveCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GraphData)) return false;
            GraphData graphData = (GraphData) o;
            return getPeriod().equals(graphData.getPeriod());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getPeriod(), getLeaveCount());
        }

        @Override
        public String toString() {
            return "GraphData{" +
                    "month='" + period + '\'' +
                    ", leaveCount=" + leaveCount +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LeaveHistoryDTO{" +
                "leaveResponseDTO=" + leaveResponseDTO +
                ", graphData=" + graphData +
                '}';
    }
}
