package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

public class InstituteDepartmentDTO {

    private Long id;

    private String departmentName;

    private Integer instituteId;

    private List<PrivilegeDTO> privileges = new ArrayList<>();

    public InstituteDepartmentDTO() {
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

    @Override
    public String toString() {
        return "InstituteDepartmentDTO{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                ", instituteId=" + instituteId +
                ", privileges=" + privileges +
                '}';
    }

    public List<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDTO> privileges) {
        this.privileges.addAll(privileges);
    }

    public void setPrivileges(PrivilegeDTO privilege) {
        this.privileges.add(privilege);
    }
}
