package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;


import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteClassSectionDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteClassSection;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InstituteClassSectionMapper {
     public void update(InstituteClassSectionDTO dto, InstituteClassSection instituteClassSectionTeacher){
          if (instituteClassSectionTeacher == null || dto == null)
               return ;

          dto.setClassName(instituteClassSectionTeacher.getInstituteClass().getClasss().getClassName());
          dto.setInstituteClassSectionId(instituteClassSectionTeacher.getId());
          dto.setInstituteName(instituteClassSectionTeacher.getInstituteClass().getInstitute().getName());
          dto.setSectionName(instituteClassSectionTeacher.getSection().getSectionName());
     }

     public InstituteClassSectionDTO toDTO(InstituteClassSection instituteClassSectionTeacher){
          if (instituteClassSectionTeacher == null)
               return null;

          InstituteClassSectionDTO dto = new InstituteClassSectionDTO();
          dto.setClassName(instituteClassSectionTeacher.getInstituteClass().getClasss().getClassName());
          dto.setInstituteClassSectionId(instituteClassSectionTeacher.getId());
          dto.setInstituteName(instituteClassSectionTeacher.getInstituteClass().getInstitute().getName());
          dto.setSectionName(instituteClassSectionTeacher.getSection().getSectionName());

          return dto;
     }

     public Set<InstituteClassSectionDTO> toDTO(Collection<InstituteClassSection> list){
          return list.stream().map(classs -> toDTO(classs)).collect(Collectors.toSet());
     }
}
