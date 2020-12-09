package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.AttendanceStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.Period;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FilterService {

    private static final Pattern SEARCH_PATTERN = Pattern.compile("(\\w+?)(:|<|>|>:|<:|!:|%|%>)(\\w+?),");

    public static Map<String, List<SearchCriteria>> createSearchMap(String[] search) {
        Map<String, List<SearchCriteria>> searchCriteriaMap = new HashMap<>();
        Map<String, List<String>> searchMap = new HashMap<>();
        if (search == null) {
            return searchCriteriaMap;
        }
        for (String s : search) {
            Matcher matcher = SEARCH_PATTERN.matcher(s + ",");
            while (matcher.find()) {
                String key = matcher.group(1);
                SearchOperation searchOperation = SearchOperation.of(matcher.group(2));
                String val = matcher.group(3);
                SearchCriteria searchCriteria = new SearchCriteria(key, val, searchOperation);
                if (searchMap.containsKey(key)) {
                    searchMap.get(key).add(val);
                    searchCriteriaMap.get(key).add(searchCriteria);
                } else {
                    searchMap.put(key.toLowerCase(Locale.ROOT), new ArrayList<>(Arrays.asList(val)));
                    searchCriteriaMap.put(key.toLowerCase(Locale.ROOT), new ArrayList<>(Arrays.asList(searchCriteria)));
                }
            }
        }
        return searchCriteriaMap;
    }

    //
    public static Specification<Student> filterStudents(Map<String, List<SearchCriteria>> searchMap, Optional<Date> startDate, Optional<Date> endDate) {
        Map<String, List<SearchCriteria>> search = searchMap;
        Specification<Student> result = Specification.where(null);

        for (Map.Entry<String, List<SearchCriteria>> entry : search.entrySet()) {
            String key = entry.getKey();
            if (key.equalsIgnoreCase("activeStatus")) {
                result = result.and(StudentSpecification
                        .studentsByActiveStatus(entry.getValue().get(0).getValue().toString().equals("Y")));
//                employeeSpecifications.add(EmployeeSpecification
//                        .employeesByActiveStatus(entry.getValue().get(0).equalsIgnoreCase("Y")));
            }
            if (key.equalsIgnoreCase("department")) {
                List<Integer> departments = entry
                        .getValue()
                        .stream()
                        .map(sc -> Integer.parseInt(sc.getValue().toString()))
                        .collect(Collectors.toList());
//                employeeSpecifications.add((Specification<Employee>) UserSpecification
//                        .userByDepartmentIdIn(departments));
                result = result.and((Specification<Student>) UserSpecification
                        .userByDepartmentIdIn(departments));
            }
            if (key.equalsIgnoreCase("class")) {
                List<Long> classes = entry
                        .getValue()
                        .stream()
                        .map(sc -> Long.parseLong(sc.getValue().toString()))
                        .collect(Collectors.toList());
                result = result.and(StudentSpecification.studentByClassIn(classes));
            }
            if (key.equalsIgnoreCase("role")) {
                List<Integer> privileges = entry
                        .getValue()
                        .stream()
                        .map(searchCriteria -> searchCriteria.getValue())
                        .map(String::valueOf)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                result = result.and(StudentSpecification.studentsByPrivilegeIn(privileges));
//                employeeSpecifications.add(EmployeeSpecification.employeesByPrivilegeIn(privileges));
            }
//            if (key.equalsIgnoreCase("designation")) {
//                GenericSpecification<Employee> specification
//                        = new GenericSpecification<>(new SearchCriteria(key, entry.getValue(), SearchOperation.IN));
//                employeeSpecifications.add(specification);
//            }
            if (key.equalsIgnoreCase("attendanceStatus")) {
                List<AttendanceStatus> attendanceStatuses = entry
                        .getValue()
                        .stream()
                        .map(searchCriteria -> searchCriteria.getValue())
                        .map(String::valueOf)
                        .map(AttendanceStatus::of)
                        .collect(Collectors.toList());
                result = result.and(StudentSpecification.studentsByAttendanceStatusIn(attendanceStatuses));
//                employeeSpecifications.add(EmployeeSpecification.employeeByAttendanceStatusIn(attendanceStatuses));
            }
//            if (key.equalsIgnoreCase("reportingManager")) {
//                List<Integer> managerIds = entry.getValue().stream().map(Integer::parseInt).collect(Collectors.toList());
//                employeeSpecifications.add(EmployeeSpecification.employeeByManagerId(managerIds));
//            }
            if (endDate.isPresent() && startDate.isPresent()) {
                result = result.and(StudentSpecification.studentWithAttendanceDateBetween(startDate.get(), endDate.get()));
            }
            if (entry.getKey().equalsIgnoreCase("userType")) {
                List<UserType> userTypes = entry
                        .getValue()
                        .stream()
                        .map(s -> s.getValue())
                        .map(String::valueOf)
                        .map(s -> UserType.of(s)).collect(Collectors.toList());
                result = result.and(new GenericSpecification<Student>(new SearchCriteria("userType", userTypes, SearchOperation.IN)));
//                employeeSpecifications.add(new GenericSpecification<Employee>(new SearchCriteria("userType", userTypes, SearchOperation.IN)));
            }
            if (entry.getKey().equalsIgnoreCase("userId")) {
                result = result.and(new GenericSpecification<Student>(new SearchCriteria("userId", entry.getValue(), SearchOperation.IN)));
//                employeeSpecifications.add(new GenericSpecification<Employee>(new SearchCriteria("userId", entry.getValue(), SearchOperation.IN)));
            }
        }
        return result;
    }

    public static Specification<Employee> filterEmployees(Map<String, List<SearchCriteria>> searchMap, Optional<Date> startDate, Optional<Date> endDate) {
        Map<String, List<SearchCriteria>> search = searchMap;
        Specification<Employee> result = Specification.where(null);

        for (Map.Entry<String, List<SearchCriteria>> entry : search.entrySet()) {
            String key = entry.getKey();
            Stream<String> stream = entry.getValue().stream().map(searchCriteria -> searchCriteria.getValue()).map(String::valueOf);
            if (key.equalsIgnoreCase("activeStatus")) {
                result = result.and(EmployeeSpecification
                        .employeesByActiveStatus(stream.findFirst().get().equalsIgnoreCase("Y")));
            }
            if (key.equalsIgnoreCase("department")) {
                List<Integer> departments = stream
                        .map(Integer::parseInt).collect(Collectors.toList());
                result = result.and((Specification<Employee>) UserSpecification
                        .userByDepartmentIdIn(departments));
            }

            if (key.equalsIgnoreCase("role")) {
                List<Integer> privileges = stream.map(Integer::parseInt).collect(Collectors.toList());
                result = result.and(EmployeeSpecification.employeesByPrivilegeIn(privileges));
            }
            if (key.equalsIgnoreCase("designation")) {
                GenericSpecification<Employee> specification
                        = new GenericSpecification<>(new SearchCriteria(key, entry.getValue(), SearchOperation.IN));
                result = result.and(specification);
            }
            if (key.equalsIgnoreCase("attendanceStatus")) {
                List<AttendanceStatus> attendanceStatuses = stream.map(AttendanceStatus::of).collect(Collectors.toList());
                result = result.and(EmployeeSpecification.employeeByAttendanceStatusIn(attendanceStatuses));
            }
            if (key.equalsIgnoreCase("reportingManager")) {
                List<Long> managerIds = stream.map(Long::parseLong).collect(Collectors.toList());
                result = result.and(EmployeeSpecification.employeeByManagerId(managerIds));
            }
            if (endDate.isPresent() && startDate.isPresent()) {
                result = result.and(EmployeeSpecification.employeeWithAttendanceDateBetween(startDate.get(), endDate.get()));
            }
            if (entry.getKey().equalsIgnoreCase("userType")) {
                List<UserType> userTypes = stream.map(s -> UserType.of(s)).collect(Collectors.toList());
                result = result.and(new GenericSpecification<Employee>(new SearchCriteria("userType", userTypes, SearchOperation.IN)));
            }
            if (entry.getKey().equalsIgnoreCase("userId")) {
                result = result.and(new GenericSpecification<Employee>(new SearchCriteria("userId", entry.getValue(), SearchOperation.IN)));
            }
        }
        return result;
    }

    public static Optional<Date> periodStartDateValue(Period period, Optional<String> value) {
        Date date = null;
        String v = value.orElseThrow(() -> new IllegalArgumentException("value of the period is not found"));
        try {
            date = period.startDate(v);
            log.debug("start date: {}", date);
        } catch (ParseException parseException) {
            throw new IllegalArgumentException("value of the period is invalid." +
                    " Expected format dd-MM-yyyy ex. 01-01-2020 translates to 1 Jan 2020");
        }
        return Optional.ofNullable(date);
    }
    public static Optional<Date> periodEndDateValue(Period period, Optional<String> value) {
        Date date = null;
        String v = value.orElseThrow(() -> new IllegalArgumentException("value of the period is not found"));
        try {
            date = period.endDate(v);
            log.debug("end date: {}", date);
        } catch (ParseException parseException) {
            throw new IllegalArgumentException("value of the period is invalid." +
                    " Expected format dd-MM-yyyy ex. 01-01-2020 translates to 1 Jan 2020");
        }
        return Optional.ofNullable(date);
    }
}
