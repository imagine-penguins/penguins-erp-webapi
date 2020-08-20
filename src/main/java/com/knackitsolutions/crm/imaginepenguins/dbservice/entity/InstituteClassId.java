package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InstituteClassId implements Serializable {
    @Column(name = "institute_id")
    Integer instituteId;
    @Column(name = "class_id")
    Long classId;

    public InstituteClassId() {
    }

    public InstituteClassId(Integer instituteId, Long classId) {
        this.instituteId = instituteId;
        this.classId = classId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstituteClassId)) return false;
        InstituteClassId that = (InstituteClassId) o;
        return Objects.equals(instituteId, that.instituteId) &&
                Objects.equals(classId, that.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instituteId, classId);
    }

    @Override
    public String toString() {
        return "InstituteClassId{" +
                "instituteId=" + instituteId +
                ", classId=" + classId +
                '}';
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
