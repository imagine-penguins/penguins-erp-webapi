package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

public class AddressDTO {

    String line1;

    String line2;

    String state;

    String zipcode;

    String country;

    public AddressDTO() {
    }

    public AddressDTO(String line1, String line2, String state, String zipcode, String country) {
        this.line1 = line1;
        this.line2 = line2;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
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

    @Override
    public String toString() {
        return "AddressDTO{" +
                "line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
