package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {
    List<Specification<T>> specifications;
    List<Boolean> isOrPredicates;
    public SpecificationBuilder() {
        specifications = new ArrayList<>();
        isOrPredicates = new ArrayList<>();
    }

    public SpecificationBuilder(List<Specification<T>> specifications) {
        this.specifications = specifications;
        isOrPredicates = new ArrayList<>();
        for (int i = 0; i < specifications.size(); i++) {
            isOrPredicates.add(Boolean.FALSE);
        }
    }

    public SpecificationBuilder<T> and(Specification<T> specification) {
        this.specifications.add(specification);
        isOrPredicates.add(Boolean.FALSE);
        return this;
    }

    public SpecificationBuilder<T> or(Specification<T> specification) {
        this.specifications.add(specification);
        isOrPredicates.add(Boolean.TRUE);
        return this;
    }

    public Specification<T> build() {
        if (specifications == null || specifications.isEmpty())
            return null;
        Specification<T> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = isOrPredicates
                    .get(i)
                    .booleanValue()
                    ? Specification.where(result).or(specifications.get(i))
                    : Specification.where(result).and(specifications.get(i));
        }
        return result;
    }

}
