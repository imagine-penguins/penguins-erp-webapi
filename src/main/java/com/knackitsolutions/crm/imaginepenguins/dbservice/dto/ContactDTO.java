package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;


public class ContactDTO {
    String phone;

    String email;

    String alternatePhone;

    String alternateEmail;

    public ContactDTO(){}

    public ContactDTO(String phone, String email) {
        this();
        this.phone = phone;
        this.email = email;
    }

    public ContactDTO(String phone, String email, String alternatePhone, String alternateEmail) {
        this(phone, email);
        this.alternatePhone = alternatePhone;
        this.alternateEmail = alternateEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    @Override
    public String toString() {
        return "ContactDTO{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", alternatePhone='" + alternatePhone + '\'' +
                ", alternateEmail='" + alternateEmail + '\'' +
                '}';
    }
}
