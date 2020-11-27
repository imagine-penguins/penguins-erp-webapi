package com.knackitsolutions.crm.imaginepenguins.dbservice.common.sort;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.SortField;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSort {
    private List<SortField> sortFields;
    private SortOrder sortOrder = SortOrder.ASCENDING;
}
