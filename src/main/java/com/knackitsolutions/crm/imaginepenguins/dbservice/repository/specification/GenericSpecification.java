package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1900581010229669687L;

    private SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
        if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
            return builder.greaterThan(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
            return builder.lessThan(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
            return builder.notEqual(
                    root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
            return builder.equal(
                    root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toLowerCase() + "%");
        } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
            return builder.like(
                    builder.lower(root.get(criteria.getKey())),
                    criteria.getValue().toString().toLowerCase() + "%");
        } else if (criteria.getOperation().equals(SearchOperation.IN)){
            CriteriaBuilder.In<Object> in = builder.in(root.get(criteria.getKey()));
            List<Object> objects = (List<Object>) criteria.getValue();
            objects.forEach(in::value);
            return in;
        }

        return null;
    }

}
