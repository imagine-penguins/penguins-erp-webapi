package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.AttendanceController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.StudentController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ClassSectionSubjectMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.TeacherLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ClassSectionSubjectDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.TeacherLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Class;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSectionSubject;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TeacherFacade {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassSectionSubjectMapper mapper;

    @Autowired
    private TeacherLoginResponseMapper teacherLoginResponseMapper;

    @Autowired
    private ClassSectionSubjectMapper classSectionSubjectMapper;

    public TeacherLoginResponseDTO findById(Long id){
        return teacherLoginResponseMapper.teacherEntityToTeacherDTO(teacherService.findById(id));
    }

    private ClassSectionSubjectDTO.MyClassDTO myClass(InstituteClassSection classSection) {
        ClassSectionSubjectDTO.MyClassDTO myClassDTO = mapper.entityToDTO(classSection);
        Long classSectionId = classSection.getId();
        log.debug("class id: {}", classSection);
        Link link = linkTo(methodOn(AttendanceController.class).loadClassStudents(classSectionId)).withRel("class-students");
        log.debug("link: {}", link);
        myClassDTO.add(link);
        return myClassDTO;
    }

    private List<ClassSectionSubjectDTO.MyClassDTO> myClasses(Long teacherId) {
        return teacherService.loadClassSections(teacherId)
                .stream().map(this::myClass).collect(Collectors.toList());
    }

    private ClassSectionSubjectDTO.MySubjectClassDTO mySubjectClass(InstituteClassSectionSubject subjectClass) {
        ClassSectionSubjectDTO.MySubjectClassDTO mySubjectClassDTO = mapper.entityToDTO(subjectClass);
        Long classSectionID = subjectClass.getInstituteClassSection().getId();
        log.debug("class id: {}", classSectionID);
        Link link = linkTo(methodOn(AttendanceController.class).loadClassStudents(classSectionID)).withRel("class-students");
        log.debug("link: {}", link);
        mySubjectClassDTO.add(link);
        return mySubjectClassDTO;
    }

    private List<ClassSectionSubjectDTO.MySubjectClassDTO> mySubjectClasses(Long teacherId) {
        return teacherService.loadSubjectClasses(teacherId)
                .stream()
                .map(this::mySubjectClass)
                .collect(Collectors.toList());
    }
    public ClassSectionSubjectDTO loadClasses(Long teacherId) {

        List<ClassSectionSubjectDTO.MyClassDTO> myClasses = myClasses(teacherId);
        List<ClassSectionSubjectDTO.MySubjectClassDTO> mySubjectClasses = mySubjectClasses(teacherId);

        ClassSectionSubjectDTO dto = new ClassSectionSubjectDTO();
        dto.setClassDTOs(myClasses);
        dto.setSubjectClasses(mySubjectClasses);

        return dto;
    }
}
