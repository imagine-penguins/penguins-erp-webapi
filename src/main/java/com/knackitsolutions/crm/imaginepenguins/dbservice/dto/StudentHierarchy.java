package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Teacher;
import lombok.Value;

@Value
public class StudentHierarchy {

    @JsonIgnore
    private final Student self;

    @JsonIgnore
    private final Teacher teacher;

    @Value
    public static class StudentDetail {
        
    }

}
