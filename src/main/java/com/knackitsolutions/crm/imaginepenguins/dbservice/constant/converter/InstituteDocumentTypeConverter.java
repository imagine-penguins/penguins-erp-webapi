package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.AttributeConverter;

@RequestParameterConverter
@javax.persistence.Converter(autoApply = true)
public class InstituteDocumentTypeConverter implements AttributeConverter<InstituteDocumentType, String>
        , Converter<String, InstituteDocumentType> {
    @Override
    public String convertToDatabaseColumn(InstituteDocumentType attribute) {
        return attribute.getDocType();
    }

    @Override
    public InstituteDocumentType convertToEntityAttribute(String dbData) {
        return InstituteDocumentType.of(dbData);
    }

    @Override
    public InstituteDocumentType convert(String source) {
        return InstituteDocumentType.of(source);
    }
}