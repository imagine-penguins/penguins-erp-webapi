package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByUsername(String username);

    List<com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User> findByUserType(UserType userType);

    @Query(value = "Select u FROM UserDTO u WHERE u.username = :username", nativeQuery = true)
    List<com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User> findBy(@Param("username") String username);

    List<com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User> findByUserDepartmentsInstituteDepartmentId(Long departmentId);

}
