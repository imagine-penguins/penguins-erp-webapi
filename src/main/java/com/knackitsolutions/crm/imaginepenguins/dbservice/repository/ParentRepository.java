package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long>, JpaSpecificationExecutor<Parent> {

    @Query(value = "SELECT  COUNT(p) FROM Parent p JOIN p.students s " +
            "WHERE s.instituteClassSection.instituteClass.institute.id = :instituteId")
    Integer countByInstituteId(@Param("instituteId") Integer instituteId);

    @Query(value = "SELECT p FROM Parent p JOIN p.students s WHERE s.instituteClassSection.instituteClass.institute.id = :instituteId " +
            "and p.active=:active")
    Integer countByInstituteId(@Param("instituteId") Integer instituteId, @Param("active") String active);

    @Query(value = "SELECT p FROM Parent p JOIN p.students s WHERE s.instituteClassSection.instituteClass.institute.id = :instituteId")
    List<Parent> findByInstituteId(@Param("instituteId") Integer instituteId);

    @Query(value = "SELECT p FROM Parent p JOIN p.students s WHERE s.instituteClassSection.instituteClass.institute.id = :instituteId " +
            "and p.active=:active")
    List<Parent> findByInstituteId(@Param("instituteId") Integer instituteId, @Param("active") String active);

    @Override
    Page<Parent> findAll(Specification<Parent> spec, Pageable pageable);

}
