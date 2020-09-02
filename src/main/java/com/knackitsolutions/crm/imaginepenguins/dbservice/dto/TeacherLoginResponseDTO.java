package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import java.util.List;
import java.util.Set;

public class TeacherLoginResponseDTO extends EmployeeLoginResponseDTO {

    private List<String> subjects;


    private Set<InstituteClassSectionDTO> classes;

    public TeacherLoginResponseDTO() {
    }

    public TeacherLoginResponseDTO(List<String> subjects, Set<InstituteClassSectionDTO> classes) {
        this.subjects = subjects;
        this.classes = classes;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public Set<InstituteClassSectionDTO> getClasses() {
        return classes;
    }

    public void setClasses(Set<InstituteClassSectionDTO> classes) {
        this.classes = classes;
    }
}
