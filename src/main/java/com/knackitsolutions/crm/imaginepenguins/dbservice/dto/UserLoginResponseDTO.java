package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Id;
import java.util.List;

public class UserLoginResponseDTO {

    @Id
    Long id;

    String username;

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

    List<PrivilegeDTO> privileges;

    Character userType;

    public UserLoginResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDTO> privileges) {
        this.privileges = privileges;
    }

    public Character getUserType() {
        return userType;
    }

    public void setUserType(Character userType) {
        this.userType = userType;
    }
}
