package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import java.util.Objects;

public class InstituteClassSectionDTO {

    String instituteName;

    String className;

    String sectionName;

    Long instituteClassSectionId;

    public InstituteClassSectionDTO() {
    }

    public InstituteClassSectionDTO(String instituteName, String className, String sectionName, Long instituteClassSectionId) {
        this.instituteName = instituteName;
        this.className = className;
        this.sectionName = sectionName;
        this.instituteClassSectionId = instituteClassSectionId;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Long getInstituteClassSectionId() {
        return instituteClassSectionId;
    }

    public void setInstituteClassSectionId(Long instituteClassSectionId) {
        this.instituteClassSectionId = instituteClassSectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstituteClassSectionDTO)) return false;
        InstituteClassSectionDTO that = (InstituteClassSectionDTO) o;
        return Objects.equals(getInstituteName(), that.getInstituteName()) &&
                Objects.equals(getClassName(), that.getClassName()) &&
                Objects.equals(getSectionName(), that.getSectionName()) &&
                getInstituteClassSectionId().equals(that.getInstituteClassSectionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInstituteName(), getClassName(), getSectionName(), getInstituteClassSectionId());
    }

    @Override
    public String toString() {
        return "InstituteClassSectionDTO{" +
                "instituteName='" + instituteName + '\'' +
                ", className='" + className + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", instituteClassSectionId=" + instituteClassSectionId +
                '}';
    }
}
