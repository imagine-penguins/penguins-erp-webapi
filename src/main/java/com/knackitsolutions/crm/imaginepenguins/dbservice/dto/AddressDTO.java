package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDTO {

    String line1;

    String line2;

    String state;

    String zipcode;

    String country;
}
