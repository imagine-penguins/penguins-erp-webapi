package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import java.util.Date;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> employeesByActiveStatus(Boolean active) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
    }

    public static Specification<Employee> employeesByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                                .join("institute", JoinType.LEFT)
                                .get("id")
                        , instituteId
                );
    }

    public static Specification<Employee> employeesByDepartmentId(Integer departmentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                                .joinSet("userDepartments", JoinType.INNER)
                                .join("instituteDepartment", JoinType.INNER)
                                .get("id")
                        , departmentId
                );
    }

    public static Specification<Employee> employeesByDepartmentId(List<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inDepartment = criteriaBuilder
                    .in(root
                            .joinSet("userDepartments", JoinType.INNER)
                            .join("instituteDepartment", JoinType.INNER)
                            .get("id")
                    );
            departmentIds.forEach(inDepartment::value);
            return inDepartment;
        };
    }

    public static Specification<Employee> employeesByPrivilegeIn(List<Integer> privileges) {
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

    public static Specification<Employee> employeeByAttendanceStatusIn(List<AttendanceStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<AttendanceStatus> attendanceStatusIn = criteriaBuilder.in(root
                    .joinSet("employeeAttendances", JoinType.LEFT)
                    .join("attendance", JoinType.LEFT).get("attendanceStatus"));
            statuses.forEach(attendanceStatusIn::value);
            return attendanceStatusIn;
        };
    }

    public static Specification<Employee> employeeByAttendanceStatus(AttendanceStatus status) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(
                    root
                            .joinSet("employeeAttendances", JoinType.INNER)
                            .join("attendance", JoinType.LEFT).get("attendanceStatus")
                    , status
            );

    }

    public static Specification<Employee> employeeByManagerId(List<Long> managers) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Long> inManager = criteriaBuilder
                    .in(root.join("manager").get("id"));
            managers.forEach(inManager::value);
            return inManager;
        };
    }

    public static Specification<Employee> employeeWithAttendanceDateBetween(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(
                        root
                                .joinSet("employeeAttendances", JoinType.INNER)
                                .join("attendance", JoinType.LEFT)
                                .get("attendanceDate")
                        , startDate
                        , endDate);
    }
}
