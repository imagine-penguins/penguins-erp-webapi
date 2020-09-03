package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class InstituteDTO extends RepresentationModel<InstituteDTO> {

    private Integer id;

    private String name;

    private InstituteType instituteType;

    private String recognitionNumber;

    private Date openTime;

    private Date closeTime;

    private String logoImg;

    private AddressDTO address;


    private ContactDTO contact;

    public InstituteDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InstituteType getInstituteType() {
        return instituteType;
    }

    public void setInstituteType(InstituteType instituteType) {
        this.instituteType = instituteType;
    }

    public String getRecognitionNumber() {
        return recognitionNumber;
    }

    public void setRecognitionNumber(String recognitionNumber) {
        this.recognitionNumber = recognitionNumber;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }
}
