package com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance;

import java.util.Optional;

public class StudentAttendanceResponseDTO extends UserAttendanceResponseDTO {

    private String rollNumber;

    private Optional<Long> attendanceId;

    public StudentAttendanceResponseDTO() {
    }

    public StudentAttendanceResponseDTO(Long id, String firstName, String lastName, String profilePic, String rollNumber) {
        super(id, firstName, lastName, profilePic);
        this.rollNumber = rollNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public Optional<Long> getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Optional<Long> attendanceId) {
        this.attendanceId = attendanceId;
    }
}
