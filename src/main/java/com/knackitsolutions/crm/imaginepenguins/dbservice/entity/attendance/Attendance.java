package com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attendance_id")
    private Long id;

    @Column(name = "load_dt_tm", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date attendanceDate;

    @Column(name = "status", nullable = false)
    private AttendanceStatus attendanceStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User supervisor;

    @OneToMany(mappedBy = "attendance")
    private Set<StudentAttendance> studentAttendances = new HashSet<>();

    @OneToMany(mappedBy = "attendance")
    private Set<EmployeeAttendance> employeeAttendances = new HashSet<>();

    @Column(name = "update_dt_tm", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public Attendance() {
        updateTime = new Date(System.currentTimeMillis());
    }

    public Attendance(Date attendanceDate, AttendanceStatus attendanceStatus) {
        this();
        this.attendanceDate = attendanceDate;
        this.attendanceStatus = attendanceStatus;
    }

    public Attendance(Long id, Date attendanceDate, AttendanceStatus attendanceStatus) {
        this();
        this.id = id;
        this.attendanceDate = attendanceDate;
        this.attendanceStatus = attendanceStatus;
    }

    public Attendance(Long id, Date attendanceDate, AttendanceStatus attendanceStatus, User supervisor) {
        this(id, attendanceDate, attendanceStatus);
        this.supervisor = supervisor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public void setEmployeeAttendance(EmployeeAttendance employeeAttendance) {
        this.employeeAttendances.add(employeeAttendance);
        employeeAttendance.setAttendance(this);
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public Set<StudentAttendance> getStudentAttendances() {
        return studentAttendances;
    }

    public void setStudentAttendance(Set<StudentAttendance> studentAttendances) {
        studentAttendances.forEach(this::setStudentAttendance);
    }

    public void setStudentAttendance(StudentAttendance studentAttendance) {
        this.studentAttendances.add(studentAttendance);
        studentAttendance.setAttendance(this);
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<EmployeeAttendance> getEmployeeAttendances() {
        return employeeAttendances;
    }

    public void setEmployeeAttendances(Set<EmployeeAttendance> employeeAttendances) {
        employeeAttendances.forEach(this::setEmployeeAttendance);
    }

    public void setEmployeeAttendances(EmployeeAttendance employeeAttendance) {
        this.employeeAttendances.add(employeeAttendance);
        employeeAttendance.setAttendance(this);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                ", attendanceDate=" + attendanceDate +
                ", attendanceStatus=" + attendanceStatus +
                ", updateTime=" + updateTime +
                '}';
    }
}
