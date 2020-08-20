package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
}
