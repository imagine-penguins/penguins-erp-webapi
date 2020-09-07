package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartmentPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteDepartmentPrivilegeRepository extends JpaRepository<InstituteDepartmentPrivilege, Long> {
    List<InstituteDepartmentPrivilege> findByInstituteDepartmentId(Long id);
}
