package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.Expression;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private Object value;
    private SearchOperation operation;
}
