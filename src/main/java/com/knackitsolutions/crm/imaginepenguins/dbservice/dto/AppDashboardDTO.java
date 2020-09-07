package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import java.util.ArrayList;
import java.util.List;

public class AppDashboardDTO {

    private List<PrivilegeDTO> privileges = new ArrayList<>(10);

    public List<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDTO> privileges) {
        this.privileges.addAll(privileges);
    }

    public void setPrivileges(PrivilegeDTO privilegeDTO){
        this.privileges.add(privilegeDTO);
    }
}
