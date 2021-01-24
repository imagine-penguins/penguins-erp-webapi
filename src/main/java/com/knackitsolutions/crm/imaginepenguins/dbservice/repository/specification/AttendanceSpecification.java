package com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification;

import com.knackitsolutions.crm.imaginepenguins.dbservice.config.DatesConfig;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AttendanceSpecification {

    public static Specification<Attendance> attendanceBySupervisorId(Long supervisorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .join("supervisor")
                        .get("id"), supervisorId
        );
    }

/*    public static Specification<StudentAttendance> studentAttendanceBySupervisorId(Long id) {
        return (Specification<StudentAttendance>) attendanceBySupervisorId(id);
    }

    public static Specification<EmployeeAttendance> employeeAttendanceBySupervisorId(Long id) {
        return (Specification<EmployeeAttendance>) attendanceBySupervisorId(id);
    }*/

    public static Specification<Attendance> attendanceByDepartmentIds(Stream<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> sDepartment = criteriaBuilder.in(
                    root
                            .joinSet("studentAttendances")
                            .join("student")
                            .joinSet("userDepartments")
                            .join("instituteDepartment", JoinType.INNER)
                            .get("id")
            );
            CriteriaBuilder.In<Integer> eDepartment = criteriaBuilder.in(
                    root
                            .joinSet("employeeAttendances")
                            .join("employee")
                            .joinSet("userDepartments")
                            .join("instituteDepartment", JoinType.INNER)
                            .get("id")
            );
            departmentIds.forEach(id -> {
                sDepartment.value(id);
                eDepartment.value(id);
            });
            return criteriaBuilder.and(sDepartment, eDepartment);
        };
    }
/*

    public static Specification<EmployeeAttendance> employeeAttendanceByDepartmentIds(List<Long> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Long> department = criteriaBuilder.in(
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
*/

    public static Specification<Attendance> attendanceByPrivilegeIds(List<Integer> privilegeIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Integer> sPrivilege = criteriaBuilder.in(root
                    .joinSet("studentAttendances")
                    .join("student")
                    .joinList("userPrivileges", JoinType.INNER)
                    .join("departmentPrivilege", JoinType.INNER)
                    .join("privilege", JoinType.INNER)
                    .get("id"));
            CriteriaBuilder.In<Integer> ePrivilege = criteriaBuilder.in(root
                    .joinSet("studentAttendances")
                    .join("student")
                    .joinList("userPrivileges", JoinType.INNER)
                    .join("departmentPrivilege", JoinType.INNER)
                    .join("privilege", JoinType.INNER)
                    .get("id"));

            privilegeIds.forEach(integer -> {
                sPrivilege.value(integer);
                ePrivilege.value(integer);
            });
            return criteriaBuilder.and(sPrivilege, ePrivilege);
        };
    }

    /*public static Specification<StudentAttendance> employeeAttendanceByPrivilegeIds(List<Integer> privilegeIds) {
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
    }*/

    public static Specification<Attendance> attendanceByAttendanceStatusIn(List<AttendanceStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<AttendanceStatus> statusIn = criteriaBuilder.in(
                    root
                            .get("attendanceStatus"));
            statuses.forEach(statusIn::value);
            return statusIn;
        };
    }
/*

    public static Specification<StudentAttendance> studentAttendanceByAttendanceStatusIn(List<AttendanceStatus> statuses) {
        return (Specification<StudentAttendance>) attendanceByAttendanceStatusIn(statuses);
    }

    public static Specification<EmployeeAttendance> employeeAttendanceByAttendanceStatusIn(List<AttendanceStatus> statuses) {
        return (Specification<EmployeeAttendance>) attendanceByAttendanceStatusIn(statuses);
    }
*/

    public static Specification<Attendance> attendanceByClass(Stream<Long> classSectionIds) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Long> inClassSection = criteriaBuilder.in(
                    root
                            .joinSet("studentAttendances")
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

    public static Specification<Attendance> attendanceWithAttendanceDate(Date startDate, SearchOperation searchOperation) {
        return (root, query, criteriaBuilder) -> {
            Expression expression = root
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

    public static Specification<Attendance> attendanceByInstituteId(Integer instituteId) {

        return (root, query, criteriaBuilder) -> {
            Join<Attendance, Employee> employeeRoot = criteriaBuilder.treat(root.join("supervisor"), Employee.class);
            return criteriaBuilder.equal(employeeRoot
                    .join("institute", JoinType.LEFT).get("id"), instituteId);
        };
    }

    public static Specification<Attendance> filterStudentAttendance(Map<String, List<SearchCriteria>> searchMap) throws ParseException {
        Map<String, List<SearchCriteria>> search = searchMap;
        Specification<Attendance> result = Specification.where(null);

        for (Map.Entry<String, List<SearchCriteria>> entry : search.entrySet()) {
            String key = entry.getKey();
            SearchCriteria firstValue = entry.getValue().stream().findFirst().get();
            Stream<String> valueStream = entry
                    .getValue().stream().map(searchCriteria -> searchCriteria.getValue()).map(o -> o.toString());
            if (key.equalsIgnoreCase("department")) {
                Stream<Integer> departments = valueStream.map(Integer::parseInt);
                result = result.and(AttendanceSpecification.attendanceByDepartmentIds(departments));
            }else if (key.equalsIgnoreCase("class")) {
                Stream<Long> classes = valueStream.map(Long::parseLong);
                result = result.and(AttendanceSpecification.attendanceByClass(classes));
            }else if (key.equalsIgnoreCase("role")) {
                List<Integer> privileges = valueStream
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                result = result.and(AttendanceSpecification.attendanceByPrivilegeIds(privileges));
            }else if (key.equalsIgnoreCase("attendanceStatus")) {
                List<AttendanceStatus> attendanceStatuses = valueStream
                        .map(AttendanceStatus::of)
                        .collect(Collectors.toList());
                result = result.and(AttendanceSpecification.attendanceByAttendanceStatusIn(attendanceStatuses));
            } else if (key.equalsIgnoreCase("attendanceDate")) {
                result = result.and(AttendanceSpecification.attendanceWithAttendanceDate(DatesConfig.format(firstValue.getValue().toString())
                        , firstValue.getOperation()));
            }/*else if (entry.getKey().equalsIgnoreCase("userType")) {
                List<UserType> userTypes = valueStream
                        .map(UserType::of).collect(Collectors.toList());
                result = result.and(
                        new GenericSpecification<>(new SearchCriteria("userType", userTypes, SearchOperation.IN))
                );
            }*/
            if (entry.getKey().equalsIgnoreCase("userId")) {
                result = result.and(new GenericSpecification<>(new SearchCriteria("id", entry.getValue(), SearchOperation.IN)));
            }
        }
        return result;
    }

}
