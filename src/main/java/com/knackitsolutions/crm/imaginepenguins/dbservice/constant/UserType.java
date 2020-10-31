package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum UserType {
    EMPLOYEE("E"),
    PARENT("P"),
    STUDENT("S");

    @JsonProperty
    private String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    @JsonValue
    public String getUserType() {
        return userType;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserType of(@JsonProperty String userType){
        return Stream.of(UserType.values())
                .filter(type -> type.getUserType().equalsIgnoreCase(userType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
