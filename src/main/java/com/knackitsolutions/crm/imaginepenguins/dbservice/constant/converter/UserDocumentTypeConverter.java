package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.AttributeConverter;

@RequestParameterConverter
@javax.persistence.Converter(autoApply = true)
public class UserDocumentTypeConverter implements Converter<String, UserDocumentType>
        , AttributeConverter<UserDocumentType, String> {

    @Override
    public String convertToDatabaseColumn(UserDocumentType attribute) {
        return attribute.getDocType();
    }

    @Override
    public UserDocumentType convertToEntityAttribute(String dbData) {
        return UserDocumentType.of(dbData);
    }

    @Override
    public UserDocumentType convert(String source) {
        return UserDocumentType.of(source);
    }
}

