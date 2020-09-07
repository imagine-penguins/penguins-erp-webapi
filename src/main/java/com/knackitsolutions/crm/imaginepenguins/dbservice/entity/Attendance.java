package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date attendanceTime;
    private AttendanceStatus attendanceStatus;

    @OneToMany(mappedBy = "attendance")
    private Set<StudentAttendance> studentAttendances = new HashSet<>();

    @OneToMany(mappedBy = "attendance")
    private Set<EmployeeAttendance> employeeAttendances = new HashSet<>();

    public Attendance(Long id, Date attendanceTime, AttendanceStatus attendanceStatus) {
        this.id = id;
        this.attendanceTime = attendanceTime;
        this.attendanceStatus = attendanceStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(Date attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public void addStudentAttendance(StudentAttendance studentAttendance) {
        this.studentAttendances.add(studentAttendance);
        studentAttendance.setAttendance(this);
    }

    public void addEmployeeAttendance(EmployeeAttendance employeeAttendance) {
        this.employeeAttendances.add(employeeAttendance);
        employeeAttendance.setAttendance(this);
    }
}
