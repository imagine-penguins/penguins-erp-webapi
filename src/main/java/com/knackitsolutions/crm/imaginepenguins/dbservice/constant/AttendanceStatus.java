package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AttendanceStatus {
    PRESENT("P"), ABSENT("A"), LEAVE("L");

    @JsonProperty
    private String status;

    AttendanceStatus(String status){
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AttendanceStatus of(@JsonProperty String status){
        return Stream.of(AttendanceStatus.values())
                .filter(value -> value.getStatus().equals(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
