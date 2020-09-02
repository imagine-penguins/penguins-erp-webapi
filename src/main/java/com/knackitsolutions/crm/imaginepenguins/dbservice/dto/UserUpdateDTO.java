package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import javax.persistence.Id;
import java.util.List;

public class UserUpdateDTO {
    @Id
    Long id;

    String password;

    String email;

    String phone;

    String alternatePhone;

    String alternateEmail;

    List<Integer> privileges;

}
