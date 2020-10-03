package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, EmployeeAttendanceKey> {

    List<EmployeeAttendance> findByEmployeeAttendanceKeyEmployeeId(Long employeeId);
    List<EmployeeAttendance> findByEmployeeAttendanceKeyEmployeeIdAndAttendanceAttendanceDateBetween(Long employeeId
            , Date updateTimeStart, Date updateTimeEnd);
}
