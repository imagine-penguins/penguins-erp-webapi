package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

public class StudentLoginResponseDTO extends UserLoginResponseDTO {

    private Long parentId;

    private InstituteClassSectionDTO classs;

    public StudentLoginResponseDTO() {
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public InstituteClassSectionDTO getClasss() {
        return classs;
    }

    public void setClasss(InstituteClassSectionDTO classs) {
        this.classs = classs;
    }
}
