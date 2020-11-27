package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDTO {
    private String phone;
    private String email;
    private String alternatePhone;
    private String alternateEmail;

    public ContactDTO(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public ContactDTO(String phone, String email, String alternatePhone, String alternateEmail) {
        this(phone, email);
        this.alternatePhone = alternatePhone;
        this.alternateEmail = alternateEmail;
    }
}
