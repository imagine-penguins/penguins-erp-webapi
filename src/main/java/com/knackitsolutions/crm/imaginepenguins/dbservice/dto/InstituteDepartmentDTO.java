package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import javax.persistence.Id;
import java.util.List;

public class InstituteDepartmentDTO {
    @Id
    Long id;

    String departmentName;

    Integer instituteId;

    List<Integer> privileges;

    protected InstituteDepartmentDTO() {
    }

    public InstituteDepartmentDTO(String departmentName, Integer instituteId) {
        this();
        this.departmentName = departmentName;
        this.instituteId = instituteId;
    }

    public InstituteDepartmentDTO(String departmentName, Integer instituteId, List<Integer> privileges) {
        this(departmentName, instituteId);
        this.privileges = privileges;
    }

    public InstituteDepartmentDTO(Long id, String departmentName, Integer instituteId, List<Integer> privileges) {
        this(departmentName, instituteId, privileges);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }

    public List<Integer> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Integer> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "InstituteDepartmentDTO{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                ", instituteId=" + instituteId +
                ", privileges=" + privileges +
                '}';
    }
}
