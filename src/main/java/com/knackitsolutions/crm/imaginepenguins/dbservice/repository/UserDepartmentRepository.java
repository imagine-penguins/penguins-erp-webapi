package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {

    UserDepartment findByUserIdAndInstituteDepartmentId(Long userId, Long departmentId);

    List<UserDepartment> findByUserId(Long userId);
}
