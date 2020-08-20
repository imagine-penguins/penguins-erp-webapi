package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Integer>{
	
}
