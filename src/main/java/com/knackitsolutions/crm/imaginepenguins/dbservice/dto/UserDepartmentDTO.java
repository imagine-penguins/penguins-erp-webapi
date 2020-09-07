package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import org.springframework.hateoas.RepresentationModel;

public class UserDepartmentDTO extends RepresentationModel<UserDepartmentDTO> {

    private Long id;

    private String departmentName;

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
}
