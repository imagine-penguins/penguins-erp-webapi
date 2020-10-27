package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
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
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query(value = "SELECT COUNT(s) FROM Student s WHERE s.instituteClassSection.instituteClass.institute.id = :instituteId")
    Integer countByInstituteId(@Param("instituteId") Integer instituteId);

    List<Student> findByInstituteClassSectionId(Long instituteClassSectionId);

    @Query(value = "SELECT s FROM Student s WHERE s.instituteClassSection.instituteClass.institute.id = :instituteId")
    List<Student> findByInstituteId(@Param("instituteId") Integer instituteId);

    @Override
    Page<Student> findAll(Specification<Student> specification, Pageable pageable);
}
