package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DashboardViewType {
    FIELD("F"), GRAPH("G"),
    CALENDAR("C");

    @JsonProperty
    private String dashboardViewTypeValue;

    DashboardViewType(String code) {
        this.dashboardViewTypeValue = code;
    }

    public String getDashboardViewTypeValue() {
        return dashboardViewTypeValue;
    }

    @JsonCreator
    public static DashboardViewType of(@JsonProperty String code){
        return Stream.of(DashboardViewType.values())
                .filter(dashboardViewType -> dashboardViewType.getDashboardViewTypeValue() == code)
                .findFirst()
                .get();
    }
}
