package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.DashboardViewType;

import java.util.ArrayList;
import java.util.List;

public class WebDashboardDTO {

    List<Container> containers = new ArrayList<>();

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers.addAll(containers);
    }

    public void setContainers(Container container) {
        containers.add(container);
    }
}
