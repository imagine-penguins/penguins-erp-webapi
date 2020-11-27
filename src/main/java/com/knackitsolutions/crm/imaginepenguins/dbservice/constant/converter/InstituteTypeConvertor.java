package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;

@Converter(autoApply = true)
public class InstituteTypeConvertor implements AttributeConverter<InstituteType, String>{
	@Override
	public String convertToDatabaseColumn(InstituteType attribute) {
		return attribute.getInstitutionType();
	}
	
	@Override
	public InstituteType convertToEntityAttribute(String dbData) {
		return InstituteType.of(dbData);
	}
}
