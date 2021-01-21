package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.FilterController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstituteClassSectionSubjectRepository extends JpaRepository<InstituteClassSectionSubject, Long> {
    List<InstituteClassSectionSubject> findByTeacherId(Long teacherId);
}
