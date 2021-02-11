package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.config.DatesConfig;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.EmployeeAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class AttendanceSpecification {

    public static Specification<?> attendanceBySupervisorId(Long supervisorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .join("attendance")
                        .join("supervisor")
                        .get("id"), supervisorId);
    }

    public static Specification<StudentAttendance> studentAttendanceBySupervisorId(Long id) {
        return (Specification<StudentAttendance>) attendanceBySupervisorId(id);
    }

    public static Specification<EmployeeAttendance> employeeAttendanceBySupervisorId(Long id) {
        return (Specification<EmployeeAttendance>) attendanceBySupervisorId(id);
    }

    public static Specification<StudentAttendance> studentNotById(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(
                root.join("student").get("id"), userId
        );
    }

    public static Specification<EmployeeAttendance> employeeNotById(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(
                root.join("employee").get("id"), userId
        );
    }

    public static Specification<StudentAttendance> studentAttendanceByDepartmentIds(Stream<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> sDepartment = criteriaBuilder.in(
                    root
                            .join("student")
                            .joinSet("userDepartments")
                            .join("instituteDepartment", JoinType.INNER)
                            .get("id")
            );
            /*CriteriaBuilder.In<Integer> eDepartment = criteriaBuilder.in(
                    root
                            .joinSet("employeeAttendances")
                            .join("employee")
                            .joinSet("userDepartments")
                            .join("instituteDepartment", JoinType.INNER)
                            .get("id")
            );*/
            departmentIds.forEach(id -> {
                sDepartment.value(id);
            });
            return sDepartment;
        };
    }

    public static Specification<EmployeeAttendance> employeeAttendanceByDepartmentIds(Stream<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> department = criteriaBuilder.in(
                    root
                            .join("employee")
                            .joinSet("userDepartments")
                            .join("instituteDepartment", JoinType.INNER)
                            .get("id")
            );
            departmentIds.forEach(department::value);
            return department;
        };
    }

    public static Specification<StudentAttendance> studentAttendanceByPrivilegeIds(List<Integer> privilegeIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> sPrivilege = criteriaBuilder.in(root
                    .join("student")
                    .joinList("userPrivileges", JoinType.INNER)
                    .join("departmentPrivilege", JoinType.INNER)
                    .join("privilege", JoinType.INNER)
                    .get("id"));
            /*CriteriaBuilder.In<Integer> ePrivilege = criteriaBuilder.in(root
                    .joinSet("studentAttendances")
                    .join("student")
                    .joinList("userPrivileges", JoinType.INNER)
                    .join("departmentPrivilege", JoinType.INNER)
                    .join("privilege", JoinType.INNER)
                    .get("id"));*/

            privilegeIds.forEach(integer -> {
                sPrivilege.value(integer);
            });
            return sPrivilege;
        };
    }

    public static Specification<EmployeeAttendance> employeeAttendanceByPrivilegeIds(Stream<Integer> privilegeIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inPrivilege = criteriaBuilder.in(root
                    .join("employee")
                    .joinList("userPrivileges", JoinType.INNER)
                    .join("departmentPrivilege", JoinType.INNER)
                    .join("privilege", JoinType.INNER)
                    .get("id"));
            privilegeIds.forEach(inPrivilege::value);
            return inPrivilege;
        };
    }

    public static Specification<?> attendanceByAttendanceStatusIn(Stream<AttendanceStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<AttendanceStatus> statusIn = criteriaBuilder.in(
                    root
                            .join("attendance")
                            .get("attendanceStatus"));
            statuses.forEach(statusIn::value);
            return statusIn;
        };
    }

    public static Specification<StudentAttendance> studentAttendanceByAttendanceStatusIn(Stream<AttendanceStatus> statuses) {
        return (Specification<StudentAttendance>) attendanceByAttendanceStatusIn(statuses);
    }

    public static Specification<EmployeeAttendance> employeeAttendanceByAttendanceStatusIn(Stream<AttendanceStatus> statuses) {
        return (Specification<EmployeeAttendance>) attendanceByAttendanceStatusIn(statuses);
    }

    public static Specification<StudentAttendance> studentAttendanceByClass(Stream<Long> classSectionIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Long> inClassSection = criteriaBuilder.in(
                    root
                            .join("classSection")
                            .get("id"));
            classSectionIds.forEach(inClassSection::value);
            return inClassSection;
        };
    }

    public static Specification<StudentAttendance> studentAttendanceBySubject(List<Integer> subjectIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> inClassSection = criteriaBuilder.in(
                    root
                            .join("instituteClassSectionSubject")
                            .get("id"));
            subjectIds.forEach(inClassSection::value);
            return inClassSection;
        };
    }

    public static Specification<StudentAttendance> studentAttendanceWithAttendanceDate(LocalDateTime startDate, SearchOperation searchOperation) {
        log.debug("where attendanceDate is {} {}", searchOperation.getOperation(), startDate);
        return (root, query, criteriaBuilder) -> {
            Expression expression = root
                    .join("attendance")
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

    public static Specification<EmployeeAttendance> employeeAttendanceWithAttendanceDate(LocalDateTime startDate, SearchOperation searchOperation) {
        log.debug("where attendanceDate is {} {}", searchOperation.getOperation(), startDate);
        return (root, query, criteriaBuilder) -> {
            Expression expression = root
                    .join("attendance")
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

    public static Specification<StudentAttendance> studentAttendanceByInstituteId(Integer instituteId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root
                                .join("classSection")
                                .join("instituteClass")
                                .join("institute")
                                .get("id"), instituteId);

    }

    public static Specification<EmployeeAttendance> employeeAttendanceByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .join("employee")
                        .join("institute", JoinType.LEFT)
                        .get("id"), instituteId);
    }

    public static Specification<StudentAttendance> filterStudentAttendance(Map<String, List<SearchCriteria>> searchMap) throws ParseException {
        Map<String, List<SearchCriteria>> search = searchMap;
        Specification<StudentAttendance> result = Specification.where(null);

        for (Map.Entry<String, List<SearchCriteria>> entry : search.entrySet()) {
            String key = entry.getKey();
            SearchCriteria firstValue = entry.getValue().stream().findFirst().get();
            Stream<String> valueStream = entry
                    .getValue().stream().map(searchCriteria -> searchCriteria.getValue()).map(o -> o.toString());
            if (key.equalsIgnoreCase("department")) {
                Stream<Integer> departments = valueStream.map(Integer::parseInt);
                result = result.and(AttendanceSpecification.studentAttendanceByDepartmentIds(departments));
            }else if (key.equalsIgnoreCase("class")) {
                Stream<Long> classes = valueStream.map(Long::parseLong);
                result = result.and(AttendanceSpecification.studentAttendanceByClass(classes));
            }else if (key.equalsIgnoreCase("role")) {
                List<Integer> privileges = valueStream
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                result = result.and(AttendanceSpecification.studentAttendanceByPrivilegeIds(privileges));
            }else if (key.equalsIgnoreCase("attendanceStatus")) {
                Stream<AttendanceStatus> attendanceStatuses = valueStream
                        .map(AttendanceStatus::of);
                result = result.and(AttendanceSpecification.studentAttendanceByAttendanceStatusIn(attendanceStatuses));
            } else if (key.equalsIgnoreCase("attendanceDate")) {
                result = result.and(AttendanceSpecification.studentAttendanceWithAttendanceDate(DatesConfig.formatLocalDateTime(firstValue.getValue().toString())
                        , firstValue.getOperation()));
            }else if (entry.getKey().equalsIgnoreCase("userId")) {
                result = result.and(new GenericSpecification<>(new SearchCriteria("id", entry.getValue(), SearchOperation.IN)));
            }
        }
        return result;
    }

    public static Specification<EmployeeAttendance> filterEmployeeAttendance(Map<String, List<SearchCriteria>> searchMap) throws ParseException {
        Map<String, List<SearchCriteria>> search = searchMap;
        Specification<EmployeeAttendance> result = Specification.where(null);

        for (Map.Entry<String, List<SearchCriteria>> entry : search.entrySet()) {
            String key = entry.getKey();
            SearchCriteria firstValue = entry.getValue().stream().findFirst().get();
            Stream<String> valueStream = entry
                    .getValue().stream().map(searchCriteria -> searchCriteria.getValue()).map(o -> o.toString());
            if (key.equalsIgnoreCase("department")) {
                Stream<Integer> departments = valueStream.map(Integer::parseInt);
                result = result.and(AttendanceSpecification.employeeAttendanceByDepartmentIds(departments));
            }else if (key.equalsIgnoreCase("role")) {
                Stream<Integer> privileges = valueStream
                        .map(Integer::parseInt);
                result = result.and(AttendanceSpecification.employeeAttendanceByPrivilegeIds(privileges));
            }else if (key.equalsIgnoreCase("attendanceStatus")) {
                Stream<AttendanceStatus> attendanceStatuses = valueStream
                        .map(AttendanceStatus::of);
                result = result.and(AttendanceSpecification.employeeAttendanceByAttendanceStatusIn(attendanceStatuses));
            } else if (key.equalsIgnoreCase("attendanceDate")) {
                result = result.and(AttendanceSpecification.employeeAttendanceWithAttendanceDate(DatesConfig.formatLocalDateTime(firstValue.getValue().toString())
                        , firstValue.getOperation()));
            }else if (entry.getKey().equalsIgnoreCase("userId")) {
                result = result
                        .and(new GenericSpecification<>(new SearchCriteria("id", entry.getValue(), SearchOperation.IN)));
            }
        }
        return result;
    }
}
