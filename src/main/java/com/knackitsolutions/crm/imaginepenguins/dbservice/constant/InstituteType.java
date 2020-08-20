package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InstituteType {
	SCHOOL('S'), COLLEGE('C');
	
	private static final Logger log = LoggerFactory.getLogger(InstituteType.class);
	
	private Character institutionType;
	
	private InstituteType(Character type) {
		this.institutionType = type;
	}
	
	@JsonProperty
	public Character getInstitutionType() {
		return institutionType;
	}
	
	@JsonCreator
	public static InstituteType of(@JsonProperty("institutionType")Character type) {
		log.info("institutionType: {}", type);
		return Stream.of(InstituteType.values())
		.filter(t -> t.getInstitutionType() == type)
		.findFirst()
		.orElseThrow(IllegalArgumentException::new);
	}

}
