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
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FilterService {

    private static final Pattern SEARCH_PATTERN = Pattern.compile("(\\w+?)(:|<|>|>:|<:|!:|%|%>)(\\w+?),");

    public static Map<String, List<SearchCriteria>> createSearchMap(String[] search) {
        log.debug("Creating Search Map");
        Map<String, List<SearchCriteria>> searchCriteriaMap = new HashMap<>();
        Map<String, List<String>> searchMap = new HashMap<>();
        if (search == null) {
            return searchCriteriaMap;
        }
        for (String s : search) {
            log.debug("Search: {}", s);
            Matcher matcher = SEARCH_PATTERN.matcher(s + ",");
//            log.debug("Pattern Found: {}", matcher.find());
            while (matcher.find()) {
                String key = matcher.group(1);
                SearchOperation searchOperation = SearchOperation.of(matcher.group(2));
                String val = matcher.group(3);
                SearchCriteria searchCriteria = new SearchCriteria(key, val, searchOperation);
                log.debug("Criteria is: {}", searchCriteria );
                if (searchMap.containsKey(key)) {
                    searchMap.get(key).add(val);
                    searchCriteriaMap.get(key).add(searchCriteria);
                } else {
                    searchMap.put(key.toLowerCase(Locale.ROOT), new ArrayList<>(Arrays.asList(val)));
                    searchCriteriaMap.put(key.toLowerCase(Locale.ROOT), new ArrayList<>(Arrays.asList(searchCriteria)));
                }
            }
        }
        log.debug("Search Map Created.");
        return searchCriteriaMap;
    }

    public static Optional<LocalDate> periodStartDateValue(Period period, Optional<String> value) {
        LocalDate date = null;
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
    public static Optional<LocalDate> periodEndDateValue(Period period, Optional<String> value) {
        LocalDate date = null;
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
