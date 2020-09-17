package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT COUNT(s) FROM Student s WHERE s.instituteClassSection.instituteClass.institute.id = :instituteId")
    Integer countByInstituteId(@Param("instituteId") Integer instituteId);

    @Query(value = "SELECT new com.knackitsolutions.crm.imaginepenguins.dbservice" +
            ".dto.attendance.StudentInfo(s.id, s.userProfile.firstName, s.userProfile.lastName, s.rollNumber) " +
            "from Student s WHERE s.instituteClassSection.id = :classSectionId")
    List<StudentInfo> findAllByClassSectionId(@Param("classSectionId") Long id);

}
