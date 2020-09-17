package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.attribute;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AttendanceConvertor implements AttributeConverter<AttendanceStatus, String> {

    @Override
    public String convertToDatabaseColumn(AttendanceStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public AttendanceStatus convertToEntityAttribute(String dbData) {
        return AttendanceStatus.of(dbData);
    }
}
