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

    @Column(name = "load_dt_tm")
    @Temporal(TemporalType.DATE)
    private Date attendanceDate;

    @Column(name = "status")
    private AttendanceStatus attendanceStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "supervisor_id")
    private User user;

    @OneToMany(mappedBy = "attendance")
    private Set<StudentAttendance> studentAttendances = new HashSet<>();

    @OneToMany(mappedBy = "attendance")
    private Set<EmployeeAttendance> employeeAttendances = new HashSet<>();

    @Column(name = "update_dt_tm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public Attendance() {
    }

    public Attendance(Long id, Date attendanceDate, AttendanceStatus attendanceStatus) {
        this.id = id;
        this.attendanceDate = attendanceDate;
        this.attendanceStatus = attendanceStatus;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
