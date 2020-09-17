package com.knackitsolutions.crm.imaginepenguins.dbservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatesConfig {
    @Value("${attendanceDateFormat:dd-MM-yyyy}")
    public String attendanceDateFormat;

    public DatesConfig() {
    }

    public String getAttendanceDateFormat() {
        return attendanceDateFormat;
    }

    public void setAttendanceDateFormat(String attendanceDateFormat) {
        this.attendanceDateFormat = attendanceDateFormat;
    }
}
