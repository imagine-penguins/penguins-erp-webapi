package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class StudentAttendanceRequestDTO {

    private Long subjectClassID;
    private Long supervisorId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date attendanceDate;

    private List<UserAttendanceRequestDTO> attendanceData;
    private Long classSectionId;

    public StudentAttendanceRequestDTO() {
    }

    public StudentAttendanceRequestDTO(Long classSectionSubjectId, Long supervisorId, Date attendanceDate, List<UserAttendanceRequestDTO> attendanceData) {
        this.subjectClassID = classSectionSubjectId;
        this.supervisorId = supervisorId;
        this.attendanceDate = attendanceDate;
        this.attendanceData = attendanceData;
    }

    public Long getSubjectClassID() {
        return subjectClassID;
    }

    public void setSubjectClassID(Long subjectClassID) {
        this.subjectClassID = subjectClassID;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public List<UserAttendanceRequestDTO> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<UserAttendanceRequestDTO> attendanceData) {
        this.attendanceData = attendanceData;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Long getClassSectionId() {
        return classSectionId;
    }

    public void setClassSectionId(Long classSectionId) {
        this.classSectionId = classSectionId;
    }

    @Override
    public String toString() {
        return "StudentAttendanceRequestDTO{" +
                "subjectClassID=" + subjectClassID +
                ", supervisorId=" + supervisorId +
                ", attendanceDate=" + attendanceDate +
                ", attendanceData=" + attendanceData +
                ", classSectionId=" + classSectionId +
                '}';
    }
}
