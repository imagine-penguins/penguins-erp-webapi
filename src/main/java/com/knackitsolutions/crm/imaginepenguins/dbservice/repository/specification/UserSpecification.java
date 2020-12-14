package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

    public static Specification<? extends User> userByDepartmentIdIn(Stream<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inDepartment = criteriaBuilder
                    .in(root
                            .joinSet("userDepartments", JoinType.LEFT)
                            .join("instituteDepartment", JoinType.LEFT)
                            .get("id")
                    );
            departmentIds.forEach(inDepartment::value);
            return inDepartment;
        };
    }

    public static Specification<? extends User> userByPrivileges(Stream<Integer> privileges) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inDepartment = criteriaBuilder
                    .in(root
                            .joinList("userPrivileges", JoinType.LEFT)
                            .join("departmentPrivilege", JoinType.LEFT)
                            .join("privilege", JoinType.LEFT)
                            .get("id")
                    );
            privileges.forEach(inDepartment::value);
            return inDepartment;
        };
    }

    public static Specification<User> filter(Map<String, List<SearchCriteria>> searchMap) {
        Specification<User> userSpecification = Specification.where(null);
        for (Map.Entry<String, List<SearchCriteria>> entry : searchMap.entrySet()) {
            String key = entry.getKey();
            SearchCriteria firstValue = entry.getValue().stream().findFirst().get();
            Stream<String> valueStream = entry
                    .getValue().stream().map(searchCriteria -> searchCriteria.getValue()).map(o -> o.toString());
            if (key.equalsIgnoreCase("department")) {
                userSpecification = userSpecification
                        .and((Specification<User>) userByDepartmentIdIn(valueStream.map(Integer::parseInt)));
            } else if (key.equalsIgnoreCase("userType")) {
                userSpecification = userSpecification.and(
                        new GenericSpecification<>(
                                new SearchCriteria("userType", valueStream, SearchOperation.IN)
                        )
                );

            } else if (key.equalsIgnoreCase("privilege")) {
                Stream<Integer> codeStream = valueStream.map(Integer::parseInt);
                userSpecification = userSpecification.and((Specification<User>) userByPrivileges(codeStream));
            } else if (key.equalsIgnoreCase("designation")) {
            }

        }
        return userSpecification;
    }
}
