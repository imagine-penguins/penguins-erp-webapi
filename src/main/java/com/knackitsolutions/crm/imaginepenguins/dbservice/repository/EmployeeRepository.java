package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByInstituteId(Integer instituteId);

//    @Query(value = "SELECT COUNT(e) FROM EMPLOYEES e WHERE U.INSTITUTE ")
    Integer countByInstituteId(Integer instituteId);

    @Query(value = "SELECT COUNT(e) FROM Employee e WHERE e.institute.id = :instituteId and e.employeeType = :employeeType")
    Integer countEmployeesByInstituteId(@Param("instituteId") Integer instituteId, EmployeeType employeeType);

//    Integer countNonTeacherByInstituteId(Integer instituteId);

    Integer countByInstituteIdAndEmployeeType(Integer instituteId, EmployeeType employeeType);

    List<Employee> findByEmployeeType(EmployeeType employeeType);


}
