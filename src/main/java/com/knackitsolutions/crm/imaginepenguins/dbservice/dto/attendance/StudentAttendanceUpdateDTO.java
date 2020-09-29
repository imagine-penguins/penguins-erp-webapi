package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StudentAttendanceUpdateDTO {

    private Long supervisorId;
    private Long classSectionId;
    private Optional<Long> classSectionSubjectId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date attendanceDate;
    private AttendanceStatus status;

    public StudentAttendanceUpdateDTO() {
    }

    public StudentAttendanceUpdateDTO(Long supervisorId, Long classSectionId, Optional<Long> classSectionSubjectId, Date updateDate) {
        this.supervisorId = supervisorId;
        this.classSectionId = classSectionId;
        this.classSectionSubjectId = classSectionSubjectId;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    /*    public static class Student{

        private StudentAttendanceKey studentAttendanceKey;
        private AttendanceStatus attendanceStatus;

        public StudentAttendanceKey getStudentAttendanceKey() {
            return studentAttendanceKey;
        }

        public void setStudentAttendanceKey(StudentAttendanceKey studentAttendanceKey) {
            this.studentAttendanceKey = studentAttendanceKey;
        }

        public AttendanceStatus getAttendanceStatus() {
            return attendanceStatus;
        }

        public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
            this.attendanceStatus = attendanceStatus;
        }
    }*/
}
