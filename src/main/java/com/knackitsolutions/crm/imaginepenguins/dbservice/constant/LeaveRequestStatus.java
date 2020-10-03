package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum LeaveRequestStatus {
    PENDING("P"),
    APPROVED("A"),
    REJECTED("R");
    private String status;

    LeaveRequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LeaveRequestStatus of(@JsonProperty String value) {
        return Stream.of(LeaveRequestStatus.values())
                .filter(leaveRequestStatus -> leaveRequestStatus.getStatus().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
