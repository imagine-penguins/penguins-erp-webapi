package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.Expression;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private Object value;
    private SearchOperation operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchCriteria)) return false;
        SearchCriteria that = (SearchCriteria) o;
        return Objects.equals(getKey(), that.getKey()) && getOperation() == that.getOperation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue(), getOperation());
    }
}
