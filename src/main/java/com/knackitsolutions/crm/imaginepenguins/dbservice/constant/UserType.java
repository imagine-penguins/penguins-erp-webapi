package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {
    EMPLOYEE('E'),
    PARENT('P'),
    STUDENT('S');

    private Character userTypeValue;

    UserType(Character userTypeValue) {
        this.userTypeValue = userTypeValue;
    }

    @JsonProperty
    public Character getUserTypeValue() {
        return userTypeValue;
    }

    @JsonCreator
    public static UserType of(@JsonProperty Character userTypeValue){
        return Stream.of(UserType.values())
                .filter(type -> type.getUserTypeValue() == userTypeValue)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
