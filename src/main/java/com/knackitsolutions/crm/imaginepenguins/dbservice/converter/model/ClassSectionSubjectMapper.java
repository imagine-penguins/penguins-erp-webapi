package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassSectionSubjectMapper {

    private static final Logger log = LoggerFactory.getLogger(ClassSectionSubjectMapper.class);


    public ClassSectionSubjectDTO.MyClassDTO entityToDTO(InstituteClassSection classSection) {
        if (classSection == null)
            return null;
        ClassSectionSubjectDTO.MyClassDTO myClass = new ClassSectionSubjectDTO.MyClassDTO();
        myClass.setInstituteClassSectionId(classSection.getId());
        myClass.setClassName(classSection.getInstituteClass().getClasss().getClassName());
        myClass.setSectionName(classSection.getSection().getSectionName());
        return myClass;
    }

    public ClassSectionSubjectDTO.MySubjectClassDTO entityToDTO(InstituteClassSectionSubject subjectClass) {
        if (subjectClass == null) {
            return null;
        }
        ClassSectionSubjectDTO.MySubjectClassDTO mySubjectClass = new ClassSectionSubjectDTO.MySubjectClassDTO();
        mySubjectClass.setInstituteClassSectionSubjectId(subjectClass.getId());
        mySubjectClass.setClassName(subjectClass.getInstituteClassSection().getInstituteClass().getClasss().getClassName());
        mySubjectClass.setSectionName(subjectClass.getInstituteClassSection().getSection().getSectionName());
        mySubjectClass.setSubjectName(subjectClass.getSubject().getName());
        return mySubjectClass;
    }

    public ClassSectionSubjectDTO entityToDTO(List<InstituteClassSectionSubject> subjectClasses
            , List<InstituteClassSection> classes) {
        ClassSectionSubjectDTO dtos = new ClassSectionSubjectDTO();

        List<ClassSectionSubjectDTO.MyClassDTO> myClasses = new ArrayList<>();
        List<ClassSectionSubjectDTO.MySubjectClassDTO> mySubjectClasses = new ArrayList<>();

        if (subjectClasses != null) {
            subjectClasses
                    .stream()
                    .map(this::entityToDTO)
                    .collect(Collectors.toCollection(() -> mySubjectClasses));
        }

        if (classes != null) {
            classes
                    .stream()
                    .map(this::entityToDTO)
                    .collect(Collectors.toCollection(() -> myClasses));
        }

        dtos.setClassDTOs(myClasses);
        dtos.setSubjectClasses(mySubjectClasses);

        return dtos;
    }
}
