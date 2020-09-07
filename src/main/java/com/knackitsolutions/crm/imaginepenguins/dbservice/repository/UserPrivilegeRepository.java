package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {

    @Query(value = "SELECT up FROM UserPrivilege up " +
            "WHERE up.user.id = :userId " +
            "and up.departmentPrivilege.instituteDepartment.id =:departmentId ")
    List<UserPrivilege> findByUserIdAndDepartmentId(@Param("userId") Long userId, @Param("departmentId") Long departmentId);

    List<UserPrivilege> findByUserId(Long userId);
}
