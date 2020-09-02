package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

public class UserProfileDTO {

    String firstName;

    String lastName;

    ContactDTO contact;

    AddressDTO address;

    public UserProfileDTO() {
    }

    public UserProfileDTO(String firstName, String lastName, ContactDTO contact, AddressDTO address) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.address = address;
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

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
