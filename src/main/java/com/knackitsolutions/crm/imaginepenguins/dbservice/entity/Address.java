package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {
    String addressLine1;
    String addressLine2;
    String state;
    String country;
    String zipcode;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Address() {
    }

    public Address(String addressLine1, String state, String country, String zipcode) {
        this.addressLine1 = addressLine1;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }

    public Address(String addressLine1, String addressLine2, String state, String country, String zipcode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }

}