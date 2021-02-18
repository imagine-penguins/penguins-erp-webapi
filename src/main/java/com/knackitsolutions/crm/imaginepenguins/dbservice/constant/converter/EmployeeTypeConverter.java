package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lombok.extern.slf4j.Slf4j;

@Converter(autoApply = true)
@Slf4j
public class EmployeeTypeConverter implements AttributeConverter<EmployeeType, String> {

    @Override
    public String convertToDatabaseColumn(EmployeeType attribute) {
        if (attribute == null)
            return null;
        log.info("Attribute---:> {}", attribute.getEmployeeTypeValue());
        return attribute.getEmployeeTypeValue();
    }

    @Override
    public EmployeeType convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        log.info("DbData---:> {}", dbData);
        return EmployeeType.of(dbData);
    }
}
