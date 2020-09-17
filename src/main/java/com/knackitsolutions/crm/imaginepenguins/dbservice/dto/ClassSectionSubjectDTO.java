package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class ClassSectionSubjectDTO {

    private List<MyClassDTO> classDTOs;
    private List<MySubjectClassDTO> subjectClasses;

    public ClassSectionSubjectDTO() {
    }

    public List<MyClassDTO> getClassDTOs() {
        return classDTOs;
    }

    public void setClassDTOs(List<MyClassDTO> classDTOs) {
        this.classDTOs = classDTOs;
    }

    public List<MySubjectClassDTO> getSubjectClasses() {
        return subjectClasses;
    }

    public void setSubjectClasses(List<MySubjectClassDTO> subjectClasses) {
        this.subjectClasses = subjectClasses;
    }

    public static class MyClassDTO extends RepresentationModel<MyClassDTO>{
        private Long instituteClassSectionId;

        private String className;

        private String sectionName;

        public MyClassDTO() {
        }

        public MyClassDTO(Long instituteClassSectionId, String className, String sectionName) {
            this.instituteClassSectionId = instituteClassSectionId;
            this.className = className;
            this.sectionName = sectionName;
        }

        public Long getInstituteClassSectionId() {
            return instituteClassSectionId;
        }

        public void setInstituteClassSectionId(Long instituteClassSectionId) {
            this.instituteClassSectionId = instituteClassSectionId;
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

        @Override
        public String toString() {
            return "MyClassDTO{" +
                    "instituteClassSectionId=" + instituteClassSectionId +
                    '}';
        }
    }
    
    public static class MySubjectClassDTO extends RepresentationModel<MySubjectClassDTO>{

        private Long instituteClassSectionSubjectId;

        @JsonIgnore
        private String className;
        @JsonIgnore
        private String sectionName;
        @JsonIgnore
        private String subjectName;

        public MySubjectClassDTO() {
        }

        public MySubjectClassDTO(Long instituteClassSectionSubjectId, String className, String sectionName, String subjectName) {
            this.instituteClassSectionSubjectId = instituteClassSectionSubjectId;
            this.className = className;
            this.sectionName = sectionName;
            this.subjectName = subjectName;
        }

        public Long getInstituteClassSectionSubjectId() {
            return instituteClassSectionSubjectId;
        }

        public void setInstituteClassSectionSubjectId(Long instituteClassSectionSubjectId) {
            this.instituteClassSectionSubjectId = instituteClassSectionSubjectId;
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

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        @Override
        public String toString() {
            return "MySubjectClassDTO{" +
                    "instituteClassSectionSubjectId=" + instituteClassSectionSubjectId +
                    ", className='" + className + '\'' +
                    ", sectionName='" + sectionName + '\'' +
                    ", subjectName='" + subjectName + '\'' +
                    '}';
        }
    }
}
