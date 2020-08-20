package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import javax.persistence.Id;
import java.util.List;

public class UserUpdateDTO {
    @Id
    Long id;

    String addressLine1;

    String addressLine2;

    String state;

    String country;

    String zipcode;

    String password;

    String email;

    String phone;

    String alternatePhone;

    String alternateEmail;

    List<Integer> privileges;

}
