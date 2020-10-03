package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import javax.persistence.AttributeConverter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;

public class InstituteTypeConvertor implements AttributeConverter<InstituteType, Character>{
	@Override
	public Character convertToDatabaseColumn(InstituteType attribute) {
		return attribute.getInstitutionType();
	}
	
	@Override
	public InstituteType convertToEntityAttribute(Character dbData) {
		// TODO Auto-generated method stub
		return InstituteType.of(dbData);
	}
}
