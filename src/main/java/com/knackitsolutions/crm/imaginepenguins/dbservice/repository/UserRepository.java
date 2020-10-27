package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User, Long> {

    com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User findByUsername(String username);

    List<com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User> findByUserType(UserType userType);

    @Query(value = "Select u FROM UserDTO u WHERE u.username = :username", nativeQuery = true)
    List<com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User> findBy(@Param("username") String username);

    List<com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User> findByUserDepartmentsInstituteDepartmentId(Long departmentId);

}
