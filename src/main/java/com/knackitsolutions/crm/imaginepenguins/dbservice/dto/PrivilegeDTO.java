package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import javax.persistence.Id;

public class PrivilegeDTO {

    private Integer id;

    private String name;

    public PrivilegeDTO() {
    }

    public PrivilegeDTO(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
