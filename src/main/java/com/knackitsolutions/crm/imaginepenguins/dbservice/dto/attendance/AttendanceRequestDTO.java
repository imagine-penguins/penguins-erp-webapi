package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.config.DatesConfig;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AttendanceRequestDTO {

    private Long subjectClassID;
    private Long supervisorId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;
    private List<StudentAttendanceRequestDTO> attendanceData;
    private Long classSectionId;

    public AttendanceRequestDTO() {
    }

    public AttendanceRequestDTO(Long classSectionSubjectId, Long supervisorId, Date date, List<StudentAttendanceRequestDTO> attendanceData) {
        this.subjectClassID = classSectionSubjectId;
        this.supervisorId = supervisorId;
        this.date = date;
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

    public List<StudentAttendanceRequestDTO> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<StudentAttendanceRequestDTO> attendanceData) {
        this.attendanceData = attendanceData;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getClassSectionId() {
        return classSectionId;
    }

    public void setClassSectionId(Long classSectionId) {
        this.classSectionId = classSectionId;
    }
}
