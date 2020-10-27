package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;

import java.util.List;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Integer>{


}
