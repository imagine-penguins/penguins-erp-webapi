package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import java.util.List;

public class UserSpecification {
    public static Specification<? extends User> userByDepartmentId(Integer departmentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                                .join("userDepartments", JoinType.LEFT)
                                .join("instituteDepartment", JoinType.LEFT)
                                .get("id")
                        , departmentId
                );
    }

    public static Specification<? extends User> userByDepartmentIdIn(List<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inDepartment = criteriaBuilder
                    .in(root
                            .join("userDepartments", JoinType.LEFT)
                            .join("instituteDepartment", JoinType.LEFT)
                            .get("id")
                    );
            departmentIds.forEach(inDepartment::value);
            return inDepartment;
        };
    }
}
