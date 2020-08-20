package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import javax.persistence.Id;

public class PrivilegeDTO {
    @Id
    Integer id;

    String privilegeName;

    String privilegeDesc;

    public PrivilegeDTO() {
    }

    public PrivilegeDTO(String privilegeName, String privilegeDesc) {
        this.privilegeName = privilegeName;
        this.privilegeDesc = privilegeDesc;
    }

    public PrivilegeDTO(Integer id, String privilegeName, String privilegeDesc) {
        this(privilegeName, privilegeDesc);
        this.id = id;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeDesc() {
        return privilegeDesc;
    }

    public void setPrivilegeDesc(String privilegeDesc) {
        this.privilegeDesc = privilegeDesc;
    }
}
