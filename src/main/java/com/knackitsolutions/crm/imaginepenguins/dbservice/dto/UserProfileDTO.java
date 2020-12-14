package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    String firstName;

    String lastName;

    ContactDTO contact;

    AddressDTO address;

}
