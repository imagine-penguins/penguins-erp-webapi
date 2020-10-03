package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = true)
public class EmployeeTypeConverter implements AttributeConverter<EmployeeType, String> {

    private static Logger log = LoggerFactory.getLogger(EmployeeTypeConverter.class);

    @Override
    public String convertToDatabaseColumn(EmployeeType attribute) {
        log.info("Attribute---:> {}", attribute.getEmployeeTypeValue());
        return attribute.getEmployeeTypeValue();
    }

    @Override
    public EmployeeType convertToEntityAttribute(String dbData) {
        log.info("DbData---:> {}", dbData);
        return EmployeeType.of(dbData);
    }
}
