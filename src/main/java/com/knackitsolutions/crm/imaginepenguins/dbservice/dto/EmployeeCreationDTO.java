package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;

import javax.persistence.Id;
import java.util.List;

public class EmployeeCreationDTO {
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

    @JsonIgnore
    List<Integer> privileges;

    Character employeeType;

    public EmployeeCreationDTO() {
    }

    @JsonIgnore
    EmployeeType employeeTypeEnum;

    Long managerId;

    List<Long> departments;



    public EmployeeCreationDTO(Long id, String username, String password, String firstName, Boolean isAdmin, String phone, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.isAdmin = isAdmin;
        this.phone = phone;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Integer> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Integer> privileges) {
        this.privileges = privileges;
    }

    public Character getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Character employeeType) {
        this.employeeType = employeeType;
    }

    public EmployeeType getEmployeeTypeEnum() {
        return employeeTypeEnum;
    }

    public void setEmployeeTypeEnum(EmployeeType employeeTypeEnum) {
        this.employeeTypeEnum = employeeTypeEnum;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public List<Long> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Long> departments) {
        this.departments = departments;
    }

    @Override
    public String toString() {
        return "EmployeeCreationDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isAdmin=" + isAdmin +
                ", isSuperAdmin=" + isSuperAdmin +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", alternatePhone='" + alternatePhone + '\'' +
                ", alternateEmail='" + alternateEmail + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", country='" + country + '\'' +
                ", privileges=" + privileges +
                ", employeeType=" + employeeType +
                ", employeeTypeEnum=" + employeeTypeEnum +
                ", managerId=" + managerId +
                ", departments=" + departments +
                '}';
    }
}
