package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import org.springframework.core.convert.converter.Converter;

@RequestParameterConverter
public class AttendancePeriodTypeConverter implements Converter<String, Period> {
    @Override
    public Period convert(String source) {
        return Period.of(source.toUpperCase());
    }
}
