package com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.SortOrder;
import org.springframework.core.convert.converter.Converter;

@RequestParameterConverter
public class SortOrderConverter implements Converter<String, SortOrder> {
    @Override
    public SortOrder convert(String source) {
        return SortOrder.of(source);
    }
}
