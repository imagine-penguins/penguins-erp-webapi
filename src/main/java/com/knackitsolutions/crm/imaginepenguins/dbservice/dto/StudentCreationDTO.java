package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Id;
import java.util.List;

public class StudentCreationDTO {
    @Id
    Long id;

    String username;

    String password;

    String firstName;

    String lastName;

    Boolean phone;

    Boolean email;

    Boolean alternatePhone;

    Boolean alternateEmail;

    String addressLine1;

    String addressLine2;

    String state;

    String zipcode;

    String country;

    @JsonIgnore
    List<Integer> privileges;

    Long classId;

    Long parentId;
}
