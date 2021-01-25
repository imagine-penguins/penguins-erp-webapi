package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.RequestParameterConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
@RequestParameterConverter
public class LeaveTypeConverter implements AttributeConverter<LeaveType, String>
        , org.springframework.core.convert.converter.Converter<String, LeaveType> {

    @Override
    public String convertToDatabaseColumn(LeaveType attribute) {
        if (attribute == null)
            return null;
        return attribute.getType();
    }

    @Override
    public LeaveType convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return LeaveType.NONE;
        return LeaveType.of(dbData);
    }

    @Override
    public LeaveType convert(String source) {
        return LeaveType.of(source);
    }
}
