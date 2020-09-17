package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeeAttendanceKey implements Serializable {

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "attendance_id")
    private Long attendanceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeAttendanceKey)) return false;
        EmployeeAttendanceKey that = (EmployeeAttendanceKey) o;
        return employeeId.equals(that.employeeId) &&
                attendanceId.equals(that.attendanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, attendanceId);
    }

    @Override
    public String toString() {
        return "EmployeeAttendanceKey{" +
                "employeeId=" + employeeId +
                ", attendanceId=" + attendanceId +
                '}';
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }
}
