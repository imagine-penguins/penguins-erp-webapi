package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import javax.persistence.Id;
import java.util.List;

public class UserCreationDTO {
    @Id
    Long id;

    String username;

    String password;

    String firstName;

    String lastName;

    Boolean isAdmin;

    Boolean isSuperAdmin;

    String phone;

    String email;

    String alternatePhone;

    String alternateEmail;

    String addressLine1;

    String addressLine2;

    String state;

    String zipcode;

    String country;

    List<Integer> privileges;

}
