package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<InstituteDepartment, Long> {
    List<InstituteDepartment> findByInstitute(Institute institute);
}
