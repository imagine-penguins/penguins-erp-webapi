package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, EmployeeAttendanceKey>
        , JpaSpecificationExecutor<EmployeeAttendance> {

    List<EmployeeAttendance> findByEmployeeAttendanceKeyEmployeeId(Long employeeId);
    List<EmployeeAttendance> findByEmployeeAttendanceKeyEmployeeIdAndAttendanceAttendanceDateBetween(Long employeeId
            , LocalDateTime updateTimeStart, LocalDateTime updateTimeEnd);
    List<EmployeeAttendance> findByEmployeeUserDepartmentsInstituteDepartmentId(Long departmentId);
}
