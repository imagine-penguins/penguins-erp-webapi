package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Data
public class Contact {

    private String phone;
    private String email;
    private String alternatePhone;
    private String alternateEmail;

    public Contact(String phone, String email){
        this.phone = phone;
        this.email = email;
    }

    public Contact(String phone, String email, String alternatePhone, String alternateEmail) {
        this(phone, email);
        this.alternatePhone = alternatePhone;
        this.alternateEmail = alternateEmail;
    }
}
