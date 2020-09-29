package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.request;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.AttendanceController;
import org.springframework.core.convert.converter.Converter;

@RequestParameterConverter
public class AttendancePeriodTypeConverter implements Converter<String, AttendanceController.Period> {
    @Override
    public AttendanceController.Period convert(String source) {
        return AttendanceController.Period.of(source);
    }
}
