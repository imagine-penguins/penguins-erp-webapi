package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.SortField;
import org.springframework.core.convert.converter.Converter;

@RequestParameterConverter
public class SortFieldConverter implements Converter<String, SortField> {
    @Override
    public SortField convert(String source) {
        return SortField.of(source);
    }
}
