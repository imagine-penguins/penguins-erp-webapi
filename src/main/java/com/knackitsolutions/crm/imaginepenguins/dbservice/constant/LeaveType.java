package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum LeaveType {
    SICK_LEAVE("SL"),
    EARNED_LEAVE("EL"),
    COMP_OFF("CO"),
    PAID_LEAVE("PL"),
    VACATION("VL");

    private String type;

    LeaveType(String type) {
        this.type = type;
    }

    @JsonProperty
    public String getType() {
        return type;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LeaveType of(@JsonProperty String value) {
        return Stream.of(LeaveType.values())
                .filter(leaveType -> leaveType.getType().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Value is not valid for LeaveType."));
    }
}
