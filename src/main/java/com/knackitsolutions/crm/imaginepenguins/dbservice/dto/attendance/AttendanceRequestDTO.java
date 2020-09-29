package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class AttendanceRequestDTO {

    private Long subjectClassID;
    private Long supervisorId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date attendanceDate;

    private List<StudentAttendanceRequestDTO> attendanceData;
    private Long classSectionId;

    public AttendanceRequestDTO() {
    }

    public AttendanceRequestDTO(Long classSectionSubjectId, Long supervisorId, Date attendanceDate, List<StudentAttendanceRequestDTO> attendanceData) {
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

    public List<StudentAttendanceRequestDTO> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<StudentAttendanceRequestDTO> attendanceData) {
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
}
