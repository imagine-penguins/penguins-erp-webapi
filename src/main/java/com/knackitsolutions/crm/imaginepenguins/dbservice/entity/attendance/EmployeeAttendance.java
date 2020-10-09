package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;

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

    @ManyToOne
    @JoinColumn(name = "institute_department_id")
    private InstituteDepartment instituteDepartment;

    public InstituteDepartment getInstituteDepartment() {
        return instituteDepartment;
    }

    public void setInstituteDepartment(InstituteDepartment instituteDepartment) {
        this.instituteDepartment = instituteDepartment;
    }

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

    public EmployeeAttendanceKey getEmployeeAttendanceKey() {
        return employeeAttendanceKey;
    }

    public void setEmployeeAttendanceKey(EmployeeAttendanceKey employeeAttendanceKey) {
        this.employeeAttendanceKey = employeeAttendanceKey;
    }
}
