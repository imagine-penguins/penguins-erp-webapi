package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import org.springframework.hateoas.RepresentationModel;

public class PrivilegeDTO extends RepresentationModel<PrivilegeDTO> {

    private Integer id;

    private String name;

    private String logo;

    public PrivilegeDTO() {
    }

    public PrivilegeDTO(Integer id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
