package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Parent;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> studentWithAttendanceDateBetween(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(
                        root
                                .joinList("studentAttendances", JoinType.INNER)
                                .join("attendance", JoinType.INNER)
                                .get("attendanceDate")
                        , startDate
                        , endDate);
    }

    public static Specification<Student> studentWithInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .join("instituteClassSection")
                        .join("instituteClass")
                        .join("institute")
                        .get("id")
                , instituteId
        );
    }

    public static Specification<Student> studentsByAttendanceStatus(AttendanceStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root
                .joinList("studentAttendances", JoinType.INNER)
                .join("attendance", JoinType.INNER)
                .get("attendanceStatus"), status);
    }

    public static Specification<Student> studentsByAttendanceStatusIn(List<AttendanceStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<AttendanceStatus> statusIn = criteriaBuilder.in(root
                    .joinList("studentAttendances", JoinType.INNER)
                    .join("attendance", JoinType.INNER)
                    .get("attendanceStatus"));
            statuses.forEach(statusIn::value);
            return statusIn;
        };
    }

    public static Specification<Student> studentsByPrivilege(Integer privilege) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root
                .joinList("userPrivileges", JoinType.INNER)
                .join("departmentPrivilege", JoinType.INNER)
                .join("privilege", JoinType.LEFT)
                .get("id"), privilege);
    }

    public static Specification<Student> studentsByPrivilegeIn(List<Integer> privileges) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inPrivilege = criteriaBuilder.in(root
                    .joinList("userPrivileges", JoinType.INNER)
                    .join("departmentPrivilege", JoinType.INNER)
                    .join("privilege", JoinType.INNER)
                    .get("id"));
            privileges.forEach(inPrivilege::value);
            return inPrivilege;
        };
    }

    public static Specification<Student> studentsByActiveStatus(Boolean active) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .get("active")
                , active
        );
    }

    public static Specification<Student> studentByClass(Integer classSectionId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .join("instituteClassSection")
                        .get("id")
                , classSectionId
        );
    }

    public static Specification<Student> studentByClassIn(List<Long> classSectionId) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Long> studentIn = criteriaBuilder.in(
                    root
                            .join("instituteClassSection")
                            .get("id")
            );
            classSectionId.forEach(studentIn::value);
            return studentIn;
        };
    }

    public static Specification<Student> studentsByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> {
            Join<Parent, Institute> parentInstituteJoin = root
                    .join("instituteClassSection", JoinType.LEFT)
                    .join("instituteClass", JoinType.LEFT)
                    .join("institute", JoinType.LEFT);
            return criteriaBuilder.equal(parentInstituteJoin.get("id"), instituteId);
        };
    }

}
