package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.RequestParameterConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
@RequestParameterConverter
public class AttendanceStatusConvertor implements AttributeConverter<AttendanceStatus, String>, org.springframework.core.convert.converter.Converter<String, AttendanceStatus> {

    @Override
    public String convertToDatabaseColumn(AttendanceStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public AttendanceStatus convertToEntityAttribute(String dbData) {
        return AttendanceStatus.of(dbData);
    }

    @Override
    public AttendanceStatus convert(String source) {
        return AttendanceStatus.of(source);
    }
}
