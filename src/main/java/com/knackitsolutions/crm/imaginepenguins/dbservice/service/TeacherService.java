package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;

import java.util.List;

public interface TeacherService {

    Teacher newTeacher(Teacher teacher);

    Teacher findById(Long id);

    List<InstituteClassSectionSubject> loadSubjectClasses(Long id);



    List<InstituteClassSection> loadClassSections(Long id);
}
