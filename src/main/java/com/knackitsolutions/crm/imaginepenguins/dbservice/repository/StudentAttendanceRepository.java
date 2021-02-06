package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, StudentAttendanceKey>
        , JpaSpecificationExecutor<StudentAttendance> {

    List<StudentAttendance> findByClassSectionId(Long classSectionId);
    List<StudentAttendance> findByClassSectionIdAndAttendanceAttendanceDateBetween(Long classSectionId, Date updateTimeStart
            , Date updateTimeEnd);

    List<StudentAttendance> findByInstituteClassSectionSubjectId(Long instituteClassSectionSubjectId);
    List<StudentAttendance> findByInstituteClassSectionSubjectIdAndAttendanceAttendanceDateBetween(Long instituteClassSectionSubjectId
            , Date updateTimeStart, Date updateTimeEnd);

    StudentAttendance findByStudentAttendanceKey(StudentAttendanceKey studentAttendanceKey);

    List<StudentAttendance> findByStudentAttendanceKeyStudentId(Long studentId);

    List<StudentAttendance> findByStudentAttendanceKeyStudentIdAndAttendanceAttendanceDateBetween(Long studentId
            , LocalDate updateTimeStart, LocalDate updateTimeEnd);

    @Override
    Page<StudentAttendance> findAll(Specification<StudentAttendance> spec, Pageable pageable);
}
