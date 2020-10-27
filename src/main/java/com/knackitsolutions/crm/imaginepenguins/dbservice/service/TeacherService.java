package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    Teacher newTeacher(Teacher teacher);

    Teacher findById(Long id);

    List<InstituteClassSectionSubject> loadSubjectClasses(Long id);

    List<Teacher> listTeachersWith(Long instituteId, Optional<Boolean> active, Pageable pageable);

    List<InstituteClassSection> loadClassSections(Long id);
}
