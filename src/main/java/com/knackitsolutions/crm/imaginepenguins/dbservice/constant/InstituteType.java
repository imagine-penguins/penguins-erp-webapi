package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum InstituteType {
	SCHOOL("S"), COLLEGE("C");

	private static final Logger log = LoggerFactory.getLogger(InstituteType.class);

	private String institutionType;

	InstituteType(String type) {
		this.institutionType = type;
	}

	@JsonProperty
	public String getInstitutionType() {
		return institutionType;
	}

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static InstituteType of(@JsonProperty String type) {
		log.info("institutionType: {}", type);
		return Stream.of(InstituteType.values())
				.filter(t -> t.getInstitutionType().equalsIgnoreCase(type))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
