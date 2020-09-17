package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;

import javax.persistence.*;

@Entity
@Table(name = "employee_attendance")
public class EmployeeAttendance {

    @EmbeddedId
    private EmployeeAttendanceKey employeeAttendanceKey;

    @ManyToOne
    @MapsId("attendanceId")
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    private EmployeeAttendanceKey getEmployeeAttendanceKey() {
        return employeeAttendanceKey;
    }

    private void setEmployeeAttendanceKey(EmployeeAttendanceKey employeeAttendanceKey) {
        this.employeeAttendanceKey = employeeAttendanceKey;
    }
}
