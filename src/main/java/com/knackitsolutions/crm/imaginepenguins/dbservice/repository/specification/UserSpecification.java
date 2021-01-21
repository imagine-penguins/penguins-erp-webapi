package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.config.DatesConfig;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserSpecification {

    public static Specification<User> usersByDepartmentIdIn(List<Integer> departmentIds) {
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

    public static Specification<User> employeeByManagerId(List<Long> managers) {
        return (root, query, criteriaBuilder) -> {
            Root<Employee> employeeRoot = criteriaBuilder.treat(root, Employee.class);
            CriteriaBuilder.In<Long> inManager = criteriaBuilder
                    .in(employeeRoot.join("manager").get("id"));
            managers.forEach(inManager::value);
            return inManager;
        };
    }

    public static Specification<User> employeeWithAttendanceDateBetween(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) -> {
            Root<Employee> employeeRoot = criteriaBuilder.treat(root, Employee.class);
            return criteriaBuilder
                    .between(
                            employeeRoot
                                    .joinSet("employeeAttendances", JoinType.INNER)
                                    .join("attendance", JoinType.LEFT)
                                    .get("attendanceDate")
                            , startDate
                            , endDate);
        };
    }

    public static Specification<User> studentWithAttendanceDateBetween(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) -> {
            Root<Student> employeeRoot = criteriaBuilder.treat(root, Student.class);
            return criteriaBuilder
                    .between(
                            employeeRoot
                                    .joinSet("studentAttendances", JoinType.INNER)
                                    .join("attendance", JoinType.LEFT)
                                    .get("attendanceDate")
                            , startDate
                            , endDate);
        };
    }

    public static Specification<User> studentWithAttendanceDate(Date startDate, SearchOperation searchOperation) {
        return (root, query, criteriaBuilder) -> {
            Root<Student> employeeRoot = criteriaBuilder.treat(root, Student.class);
            Expression expression = employeeRoot
                    .joinSet("studentAttendances", JoinType.INNER)
                    .join("attendance", JoinType.LEFT)
                    .get("attendanceDate");
            if (searchOperation == SearchOperation.GREATER_THAN) {
                return criteriaBuilder.greaterThan(expression, startDate);
            } else if (searchOperation == SearchOperation.GREATER_THAN_EQUAL) {
                return criteriaBuilder.greaterThanOrEqualTo(expression, startDate);
            } else if (searchOperation == SearchOperation.LESS_THAN) {
                return criteriaBuilder.lessThan(expression, startDate);
            } else if (searchOperation == SearchOperation.LESS_THAN_EQUAL) {
                return criteriaBuilder.lessThanOrEqualTo(expression, startDate);
            } else {
                return criteriaBuilder.equal(expression, startDate);
            }
        };
    }
    public static Specification<User> usersByPrivilegeIn(List<Integer> privileges) {
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

    public static Specification<User> studentsByAttendanceStatus(AttendanceStatus status) {
        return (root, query, criteriaBuilder) -> {
            Root<Student> studentRoot = criteriaBuilder.treat(root, Student.class);
            return criteriaBuilder.equal(studentRoot
                    .joinList("studentAttendances", JoinType.INNER)
                    .join("attendance", JoinType.INNER)
                    .get("attendanceStatus"), status);
        };
    }

    public static Specification<User> studentsByAttendanceStatusIn(List<AttendanceStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            Root<Student> studentRoot = criteriaBuilder.treat(root, Student.class);
            CriteriaBuilder.In<AttendanceStatus> statusIn = criteriaBuilder.in(studentRoot
                    .joinList("studentAttendances", JoinType.INNER)
                    .join("attendance", JoinType.INNER)
                    .get("attendanceStatus"));
            statuses.forEach(statusIn::value);
            return statusIn;
        };
    }

    public static Specification<User> employeeByAttendanceStatusIn(List<AttendanceStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            Root<Employee> employeeRoot = criteriaBuilder.treat(root, Employee.class);
            CriteriaBuilder.In<AttendanceStatus> attendanceStatusIn = criteriaBuilder.in(employeeRoot
                    .joinSet("employeeAttendances", JoinType.LEFT)
                    .join("attendance", JoinType.LEFT).get("attendanceStatus"));
            statuses.forEach(attendanceStatusIn::value);
            return attendanceStatusIn;
        };
    }

    public static Specification<User> userByDepartmentId(Integer departmentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                                .join("userDepartments", JoinType.LEFT)
                                .join("instituteDepartment", JoinType.LEFT)
                                .get("id")
                        , departmentId
                );
    }

    public static Specification<User> userByDepartmentIdIn(Stream<Integer> departmentIds) {
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

    public static Specification<User> userByPrivileges(Stream<Integer> privileges) {
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

    public static Specification<User> studentsByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> {
            Root<Student> studentRoot = criteriaBuilder.treat(root, Student.class);
            Join<Parent, Institute> parentInstituteJoin = studentRoot
                    .join("instituteClassSection", JoinType.LEFT)
                    .join("instituteClass", JoinType.LEFT)
                    .join("institute", JoinType.LEFT);
            return criteriaBuilder.equal(parentInstituteJoin.get("id"), instituteId);
        };
    }

    public static Specification<User> studentByClassIn(List<Long> classSectionIds) {
        return (root, query, criteriaBuilder) -> {
            Root<Student> studentRoot = criteriaBuilder.treat(root, Student.class);
            CriteriaBuilder.In<Long> inClassSection = criteriaBuilder.in(
                    studentRoot
                            .join("instituteClassSection")
                            .get("id"));
            classSectionIds.forEach(inClassSection::value);
            return inClassSection;
        };
    }

    public static Specification<User> employeesByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> {
            Root<Employee> employeeRoot = criteriaBuilder.treat(root, Employee.class);
            return criteriaBuilder
                    .equal(employeeRoot
                                    .join("institute", JoinType.LEFT)
                                    .get("id")
                            , instituteId
                    );
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

    public static Specification<User> filterUsers(Map<String, List<SearchCriteria>> searchMap) throws ParseException {
        Map<String, List<SearchCriteria>> search = searchMap;
        Specification<User> result = Specification.where(null);

        for (Map.Entry<String, List<SearchCriteria>> entry : search.entrySet()) {
            String key = entry.getKey();
            SearchCriteria firstValue = entry.getValue().stream().findFirst().get();
            Stream<String> valueStream = entry
                    .getValue().stream().map(searchCriteria -> searchCriteria.getValue()).map(o -> o.toString());
            if (key.equalsIgnoreCase("activeStatus")) {
                result = result.and(
                        new GenericSpecification<>(new SearchCriteria("active", valueStream.map(Boolean::valueOf)
                                .collect(Collectors.toList()), SearchOperation.IN))
                );
            }else if (key.equalsIgnoreCase("department")) {
                Stream<Integer> departments = valueStream.map(Integer::parseInt);
                result = result.and(UserSpecification.userByDepartmentIdIn(departments));
            }else if (key.equalsIgnoreCase("class")) {
                List<Long> classes = valueStream.map(Long::parseLong)
                        .collect(Collectors.toList());
                result = result.and(UserSpecification.studentByClassIn(classes));
            }else if (key.equalsIgnoreCase("role")) {
                List<Integer> privileges = valueStream
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                result = result.and(UserSpecification.usersByPrivilegeIn(privileges));
            }else if (key.equalsIgnoreCase("designation")) {
                result = result.and(
                        new GenericSpecification<>(new SearchCriteria(key, valueStream.collect(Collectors.toList()), SearchOperation.IN))
                );
            }else if (key.equalsIgnoreCase("attendanceStatus")) {
                List<AttendanceStatus> attendanceStatuses = valueStream
                        .map(AttendanceStatus::of)
                        .collect(Collectors.toList());
                result = result.and(UserSpecification.studentsByAttendanceStatusIn(attendanceStatuses));
            } else if (key.equalsIgnoreCase("reportingManager")) {
                List<Long> managerIds = valueStream.map(Long::parseLong).collect(Collectors.toList());
                result = result.and(UserSpecification.employeeByManagerId(managerIds));
            } else if (key.equalsIgnoreCase("attendanceDate")) {
                result = result.and(UserSpecification.studentWithAttendanceDate(DatesConfig.format(firstValue.getValue().toString())
                        , firstValue.getOperation()));
            }else if (entry.getKey().equalsIgnoreCase("userType")) {
                List<UserType> userTypes = valueStream
                        .map(UserType::of).collect(Collectors.toList());
                result = result.and(
                        new GenericSpecification<>(new SearchCriteria("userType", userTypes, SearchOperation.IN))
                );
            }
            if (entry.getKey().equalsIgnoreCase("userId")) {
                result = result.and(new GenericSpecification<>(new SearchCriteria("id", entry.getValue(), SearchOperation.IN)));
            }
        }
        return result;
    }
}
