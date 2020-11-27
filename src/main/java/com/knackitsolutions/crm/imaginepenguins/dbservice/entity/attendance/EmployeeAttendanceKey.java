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
public class EmployeeAttendanceKey implements Serializable {

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "attendance_id")
    private Long attendanceId;

}
