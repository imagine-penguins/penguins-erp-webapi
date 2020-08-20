package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Contact {

    private String phone;
    private String email;

    private String alternatePhone;
    private String alternateEmail;

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }


    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    protected Contact(){

    }

    public Contact(String phone, String email){
        this.phone = phone;
        this.email = email;
    }

    public Contact(String phone, String email, String alternatePhone, String alternateEmail) {
        this(phone, email);
        this.alternatePhone = alternatePhone;
        this.alternateEmail = alternateEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return Objects.equals(phone, contact.phone) &&
                Objects.equals(email, contact.email) &&
                Objects.equals(alternatePhone, contact.alternatePhone) &&
                Objects.equals(alternateEmail, contact.alternateEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, email, alternatePhone, alternateEmail);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", alternatePhone='" + alternatePhone + '\'' +
                ", alternateEmail='" + alternateEmail + '\'' +
                '}';
    }
}
