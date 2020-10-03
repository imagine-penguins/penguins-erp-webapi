package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class PrivilegeDTO extends RepresentationModel<PrivilegeDTO> {

    private Integer id;

    private String name;

    private String bgImg;

    private List<PrivilegeDTO> privileges;

    public PrivilegeDTO() {
    }

    public PrivilegeDTO(Integer id, String name, String bgImg) {
        this.id = id;
        this.name = name;
        this.bgImg = bgImg;
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

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public List<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDTO> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "PrivilegeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bgImg='" + bgImg + '\'' +
                '}';
    }
}
