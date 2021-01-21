package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteClassSectionRepository extends JpaRepository<InstituteClassSection, Long> {

    List<InstituteClassSection> findByTeacherId(Long teacherId);

}
