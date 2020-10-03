package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StudentAttendanceUpdateRequestDTO extends UserAttendanceUpdateRequestDTO{

    private Long classSectionId;
    private Optional<Long> classSectionSubjectId;


    public StudentAttendanceUpdateRequestDTO() {
    }

    public StudentAttendanceUpdateRequestDTO(Long supervisorId, Date attendanceDate, AttendanceStatus status, Long classSectionId, Optional<Long> classSectionSubjectId) {
        super(supervisorId, attendanceDate, status);
        this.classSectionId = classSectionId;
        this.classSectionSubjectId = classSectionSubjectId;
    }

    public Long getClassSectionId() {
        return classSectionId;
    }

    public void setClassSectionId(Long classSectionId) {
        this.classSectionId = classSectionId;
    }

    public Optional<Long> getClassSectionSubjectId() {
        return classSectionSubjectId;
    }

    public void setClassSectionSubjectId(Optional<Long> classSectionSubjectId) {
        this.classSectionSubjectId = classSectionSubjectId;
    }
}
