package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
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
            ".dto.attendance.StudentAttendanceResponseDTO(s.id, s.userProfile.firstName, s.userProfile.lastName, s.userProfile.profilePic, s.rollNumber) " +
            "from Student s WHERE s.instituteClassSection.id = :classSectionId")
    List<StudentAttendanceResponseDTO> findAllByClassSectionId(@Param("classSectionId") Long id);

}
