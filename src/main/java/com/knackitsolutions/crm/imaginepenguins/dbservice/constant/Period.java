package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.stream.Stream;

public enum Period {

    MONTH("M"){
        @Override
        public LocalDate startDate(String value) {
            Month month = Month.of(Integer.parseInt(value));
            LocalDate date = LocalDate.now().withMonth(month.getValue());//(Year.now().getValue(), month, 1);
            LocalDate monthStart = date.withDayOfMonth(1);
//            LocalDate monthEnd = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
            return monthStart; //Date.from(monthStart.atStartOfDay(zoneId).toInstant());
        }

        @Override
        public LocalDate endDate(String value) {
            Month month = Month.of(Integer.parseInt(value));
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate date = LocalDate.now().withMonth(month.getValue());//(Year.now().getValue(), month, 1);
            LocalDate monthEnd = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
            return monthEnd;
        }
    },
    WEEK("W"){
        @Override
        public LocalDate startDate(String value) {
            Integer weekNumber = Integer.valueOf(value);
            LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);
            return week.with(DayOfWeek.MONDAY); //Date.from(.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        @Override
        public LocalDate endDate(String value) {
            Integer weekNumber = Integer.valueOf(value);
            LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);
            return week.with(DayOfWeek.MONDAY).plusDays(6); //Date.from(.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    },
    DAY("D"){
        @Override
        public LocalDate startDate(String value) throws ParseException {
            return getDate(value);
        }

        @Override
        public LocalDate endDate(String value) throws ParseException {
            return getDate(value);
        }
    },
    CUSTOM("C"){
        @Override
        public LocalDate startDate(String value) throws ParseException {
            return getDate(value.split(",")[0]);
        }

        @Override
        public LocalDate endDate(String value) throws ParseException {
            return getDate(value.split(",")[1]);
        }
    };
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private String period;

    Period(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public static Period of(String period) {
        return Stream.of(Period.values())
                .filter(p -> p.getPeriod().equals(period))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public LocalDate getDate(String value) throws ParseException {
        return LocalDate.parse(value, dateFormat);
    }
    abstract public LocalDate startDate(String value) throws DateTimeException, ParseException;
    abstract public LocalDate endDate(String value) throws DateTimeException, ParseException;
}
