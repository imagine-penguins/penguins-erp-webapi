package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class InstituteDTO {

    private Integer id;

    private String name;

    private InstituteType instituteType;

    private String recognitionNumber;

    @JsonFormat(pattern = "HH:mm")
    private Date openTime;

    @JsonFormat(pattern = "HH:mm")
    private Date closeTime;

    private AddressDTO address;

    private ContactDTO contact;

}
