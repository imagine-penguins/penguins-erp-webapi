package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentAttendanceKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "attendance_id")
    private Long attendanceId;

}
