package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {
}
