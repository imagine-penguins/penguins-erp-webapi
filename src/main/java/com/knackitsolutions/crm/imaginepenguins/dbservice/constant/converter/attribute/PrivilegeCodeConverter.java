package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.attribute;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PrivilegeCodeConverter implements AttributeConverter<PrivilegeCode, String> {

    @Override
    public String convertToDatabaseColumn(PrivilegeCode attribute) {
        return attribute.getPrivilegeCode();
    }

    @Override
    public PrivilegeCode convertToEntityAttribute(String dbData) {
        return PrivilegeCode.of(dbData);
    }

}
