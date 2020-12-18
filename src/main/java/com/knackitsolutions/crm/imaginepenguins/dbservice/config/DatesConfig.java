package com.knackitsolutions.crm.imaginepenguins.dbservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DatesConfig {

    @Value("${attendanceDateFormat:dd-MM-yyyy}")
    private static String attendanceDateFormat;

    public DatesConfig() {
    }

    public static String getAttendanceDateFormat() {
        return attendanceDateFormat;
    }

    public static void setAttendanceDateFormat(String attendanceDateFormat) {
        DatesConfig.attendanceDateFormat = attendanceDateFormat;
    }

    public static Date format(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(attendanceDateFormat);
        return formatter.parse(date);
    }

    public static Date now(){
        return new Date(System.currentTimeMillis());}


}
