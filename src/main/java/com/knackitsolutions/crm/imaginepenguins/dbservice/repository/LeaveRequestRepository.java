package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>, JpaSpecificationExecutor<LeaveRequest> {
    List<LeaveRequest> findByUserId(Long userId);

    @Query("select count(lr.id) from LeaveRequest lr where (lr.user.id = :userid) " +
            "and (lr.startDate <= :mydate and lr.endDate >= :mydate) and lr.leaveRequestStatus = :status")
    Long isOnLeave(@Param("userid") Long userId, @Param("mydate") LocalDateTime date, @Param("status") LeaveRequestStatus leaveRequestStatus);

    @Query("select lr from LeaveRequest lr where (lr.user.id = :userid) and (lr.startDate <= :mydate and lr.endDate >= :mydate)")
    LeaveRequest findByUserIdAndDate(@Param("userid")Long userId, @Param("mydate") LocalDateTime date);

//    @Query(value = "select lr.* from leave_requests lr join users u On lr.user_id = u.id and u.id = :userid where (lr.start_date <= ':mydate' and lr.end_date >= ':mydate')", nativeQuery = true)
////    @Query("select leavereque0_.id as id1_15_, leavereque0_.approver_user_id as approve10_15_, leavereque0_.approves_user_id as approve11_15_, leavereque0_.end_date as end_date2_15_, leavereque0_.leave_reason as leave_re3_15_, leavereque0_.request_status as request_4_15_, leavereque0_.leave_type as leave_ty5_15_, leavereque0_.rejected_reason as rejected6_15_, leavereque0_.start_date as start_da7_15_, leavereque0_.title as title8_15_, leavereque0_.update_dt_tm as update_d9_15_, leavereque0_.user_id as user_id12_15_ from leave_requests leavereque0_ where leavereque0_.user_id=19 and leavereque0_.start_date<='2021-02-06 16:36:03' and leavereque0_.end_date>='2021-02-06 16:36:03';")
//    LeaveRequest nativeFindByUserIdAndDate(@Param("userid")Long userId, @Param("mydate") String date);

}
