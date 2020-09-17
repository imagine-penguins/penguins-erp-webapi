package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.request;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import org.springframework.core.convert.converter.Converter;

@RequestParameterConverter
public class AttendanceStatusTypeConverter implements Converter<String, AttendanceStatus> {
    @Override
    public AttendanceStatus convert(String value) {
        return AttendanceStatus.of(value);
    }
}
