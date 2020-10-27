package com.knackitsolutions.crm.imaginepenguins.dbservice.constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.stream.Stream;

public enum Period {

    MONTH("M"){
        @Override
        public Date startDate(String value) {
            Month month = Month.of(Integer.parseInt(value));
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate date = LocalDate.now().withMonth(month.getValue());//(Year.now().getValue(), month, 1);
            LocalDate monthStart = date.withDayOfMonth(1);
            LocalDate monthEnd = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
            return Date.from(monthStart.atStartOfDay(zoneId).toInstant());
        }

        @Override
        public Date endDate(String value) {
            Month month = Month.of(Integer.parseInt(value));
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate date = LocalDate.now().withMonth(month.getValue());//(Year.now().getValue(), month, 1);
            LocalDate monthEnd = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
            return Date.from(monthEnd.atStartOfDay(zoneId).toInstant());
        }
    },
    WEEK("W"){
        @Override
        public Date startDate(String value) {
            Integer weekNumber = Integer.valueOf(value);
            LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);
            return Date.from(week.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        @Override
        public Date endDate(String value) {
            Integer weekNumber = Integer.valueOf(value);
            LocalDate week = LocalDate.now().with(ChronoField.ALIGNED_WEEK_OF_YEAR, weekNumber);
            return Date.from(week.with(DayOfWeek.MONDAY).plusDays(6).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    },
    DAY("D"){
        @Override
        public Date startDate(String value) throws ParseException {
            return new SimpleDateFormat("dd-MM-yyyy").parse(value);
        }

        @Override
        public Date endDate(String value) throws ParseException {
            return new SimpleDateFormat("dd-MM-yyyy").parse(value);
        }
    };
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

    abstract public Date startDate(String value) throws DateTimeException, ParseException;
    abstract public Date endDate(String value) throws DateTimeException, ParseException;
}
