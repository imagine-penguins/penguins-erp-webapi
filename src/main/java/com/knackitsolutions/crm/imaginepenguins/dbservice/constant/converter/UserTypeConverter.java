package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import javax.persistence.AttributeConverter;

public class UserTypeConverter implements AttributeConverter<UserType, Character> {
    @Override
    public Character convertToDatabaseColumn(UserType attribute) {
        return attribute.getUserTypeValue();
    }

    @Override
    public UserType convertToEntityAttribute(Character dbData) {
        return UserType.of(dbData);
    }
}
