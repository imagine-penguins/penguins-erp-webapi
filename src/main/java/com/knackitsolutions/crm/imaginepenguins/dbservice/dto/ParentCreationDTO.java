package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Id;
import java.util.List;

public class ParentCreationDTO {
    @Id
    Long id;

    String username;

    String password;

    String firstName;

    String lastName;

    String phone;

    String email;

    String alternatePhone;

    String alternateEmail;

    String addressLine1;

    String addressLine2;

    String state;

    String zipcode;

    String country;

    @JsonIgnore
    List<Integer> privileges;
}
