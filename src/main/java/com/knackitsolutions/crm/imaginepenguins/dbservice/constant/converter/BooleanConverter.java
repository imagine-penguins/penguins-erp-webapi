package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
@RequestParameterConverter
public class BooleanConverter implements AttributeConverter<Boolean, String>
        , org.springframework.core.convert.converter.Converter<String, Boolean> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null || !attribute)
            return "N";
        return "Y";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.equalsIgnoreCase("N"))
            return false;
        return dbData.equalsIgnoreCase("Y");
    }

    @Override
    public Boolean convert(String source) {
        if (source == null || source.equalsIgnoreCase("N"))
            return false;
        return source.equalsIgnoreCase("Y");
    }
}
