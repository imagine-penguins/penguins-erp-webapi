package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.AttributeConverter;

@RequestParameterConverter
@javax.persistence.Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, String>, Converter<String, UserType> {
    @Override
    public String convertToDatabaseColumn(UserType attribute) {
        return attribute.getUserType();
    }

    @Override
    public UserType convertToEntityAttribute(String dbData) {
        return UserType.of(dbData);
    }

    @Override
    public UserType convert(String source) {
        return UserType.of(source);
    }
}
