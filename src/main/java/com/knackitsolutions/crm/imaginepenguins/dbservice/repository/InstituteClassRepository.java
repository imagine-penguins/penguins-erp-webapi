package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteClassRepository extends JpaRepository<InstituteClass, Long> {
    Optional<InstituteClass> findByInstituteIdAndClasssId(Long instituteId, Long classId);

    List<InstituteClass> findByInstituteNameAndClasssClassName(String instituteName, String className);

    List<InstituteClass> findByInstituteIdAndClasssClassName(Long instituteId, String className);
}
