package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class AttendanceHistoryDTO extends RepresentationModel<AttendanceHistoryDTO> {

    private List<StudentAttendanceResponseDTO> students;
    private List<UserAttendanceResponseDTO> users;
    private GraphData graphData;

    public List<UserAttendanceResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserAttendanceResponseDTO> users) {
        this.users = users;
    }

    public List<StudentAttendanceResponseDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentAttendanceResponseDTO> students) {
        this.students = students;
    }

    public GraphData getGraphData() {
        return graphData;
    }

    public void setGraphData(GraphData graphData) {
        this.graphData = graphData;
    }

    public static class GraphData {
        private Integer presentPercent;
        private Integer absentPercent;
        private Integer leavePercent;

        public Integer getPresentPercent() {
            return presentPercent;
        }

        public void setPresentPercent(Integer presentPercent) {
            this.presentPercent = presentPercent;
        }

        public Integer getAbsentPercent() {
            return absentPercent;
        }

        public void setAbsentPercent(Integer absentPercent) {
            this.absentPercent = absentPercent;
        }

        public Integer getLeavePercent() {
            return leavePercent;
        }

        public void setLeavePercent(Integer leavePercent) {
            this.leavePercent = leavePercent;
        }
    }


}
