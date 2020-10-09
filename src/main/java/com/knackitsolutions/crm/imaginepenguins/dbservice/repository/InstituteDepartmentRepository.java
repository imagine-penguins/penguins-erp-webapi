package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituteDepartmentRepository extends JpaRepository<InstituteDepartment, Long> {
    @Override
    Optional<InstituteDepartment> findById(Long aLong);
}
