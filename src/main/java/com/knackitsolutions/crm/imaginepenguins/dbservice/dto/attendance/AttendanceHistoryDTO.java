package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class AttendanceHistoryDTO extends RepresentationModel<AttendanceHistoryDTO>{

    private List<Student> students;
    private GraphData graphData;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public GraphData getGraphData() {
        return graphData;
    }

    public void setGraphData(GraphData graphData) {
        this.graphData = graphData;
    }

    public static class Student extends RepresentationModel<Student> {
        private StudentAttendanceKey studentAttendanceKey;
        private String name;
        private String rollNumber;
        private AttendanceStatus status;

        public StudentAttendanceKey getStudentAttendanceKey() {
            return studentAttendanceKey;
        }

        public void setStudentAttendanceKey(StudentAttendanceKey studentAttendanceKey) {
            this.studentAttendanceKey = studentAttendanceKey;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(String rollNumber) {
            this.rollNumber = rollNumber;
        }

        public AttendanceStatus getStatus() {
            return status;
        }

        public void setStatus(AttendanceStatus status) {
            this.status = status;
        }
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
