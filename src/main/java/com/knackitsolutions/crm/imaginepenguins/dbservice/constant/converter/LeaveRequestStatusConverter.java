package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.RequestParameterConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
@RequestParameterConverter
public class LeaveRequestStatusConverter implements AttributeConverter<LeaveRequestStatus, String>
        , org.springframework.core.convert.converter.Converter<String, LeaveRequestStatus> {
    @Override
    public String convertToDatabaseColumn(LeaveRequestStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public LeaveRequestStatus convertToEntityAttribute(String dbData) {
        return LeaveRequestStatus.of(dbData);
    }

    @Override
    public LeaveRequestStatus convert(String source) {
        return LeaveRequestStatus.of(source);
    }

}
