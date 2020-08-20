package com.knackitsolutions.crm.imaginepenguins.dbservice.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Converter(autoApply = true)
public class EmployeeTypeConverter implements AttributeConverter<EmployeeType, Character> {
    @Override
    public Character convertToDatabaseColumn(EmployeeType attribute) {
        return attribute.getEmployeeTypeValue();
    }

    @Override
    public EmployeeType convertToEntityAttribute(Character dbData) {
        return EmployeeType.of(dbData);
    }
}
