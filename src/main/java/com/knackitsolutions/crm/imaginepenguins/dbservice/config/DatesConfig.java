package com.knackitsolutions.crm.imaginepenguins.dbservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DatesConfig {

    @Value("${attendanceDateFormat:dd-MM-yyyy}")
    private static String attendanceDateFormat;

    public DatesConfig() {
    }

//    SimpleDateFormat formatter = new SimpleDateFormat(attendanceDateFormat);
    private static DateTimeFormatter attendanceDateFormatter = DateTimeFormatter.ofPattern(attendanceDateFormat);

    public static String getAttendanceDateFormat() {
        return attendanceDateFormat;
    }

    public static void setAttendanceDateFormat(String attendanceDateFormat) {
        DatesConfig.attendanceDateFormat = attendanceDateFormat;
    }

    public static LocalDate format(String date) throws ParseException {
        return LocalDate.parse(date, attendanceDateFormatter);
    }

    public static Date now(){
        return new Date(System.currentTimeMillis());
    }

    public static LocalDateTime getLocalTimeDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate getLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
