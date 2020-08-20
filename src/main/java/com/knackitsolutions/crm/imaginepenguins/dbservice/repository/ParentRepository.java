package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
}
