package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findByInstituteId(Integer instituteId);
    Integer countByInstituteId(Integer instituteId);

    @Query(value = "select t.instituteClassSectionSubjects from Teacher t where t.id = :teacherId")
    List<InstituteClassSectionSubject> getClassSectionSubjects(@Param("teacherId") Long id);

    @Query(value = "select t.instituteClassSections from Teacher t where t.id = :teacherId")
    List<InstituteClassSection> getClassSections(@Param("teacherId") Long id);
}
