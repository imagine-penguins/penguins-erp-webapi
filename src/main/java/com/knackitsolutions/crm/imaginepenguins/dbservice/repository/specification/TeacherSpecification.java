package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class TeacherSpecification {
    public static Specification<Teacher> teachersByActive(Boolean active) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }

    public static Specification<Teacher> teachersByInstituteId(Long instituteId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                                .join("institute", JoinType.LEFT)
                                .get("id")
                        , instituteId
                );
    }
}
