package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.util.stream.Stream;

public enum AttendanceStatus {
    PRESENT("P"), ABSENT("A"), LEAVE("L");

    private String status;

    AttendanceStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public AttendanceStatus of(String status){
        return Stream.of(AttendanceStatus.values())
                .filter(value -> value.getStatus().equals(status))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
