package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUserId(Long userId);

    @Query("select lr.id from LeaveRequest lr where (lr.user.id = :userid) and (lr.startDate >= :mydate and lr.endDate <= :mydate)")
    Integer isOnLeave(@Param("userid")Long userId, @Param("mydate") Date date);

}
