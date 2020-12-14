package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private Long userId;
    private String passportPic;
    private String profilePic;
    private GeneralInformation generalInformation;
    private PersonalInformation personalInformation;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneralInformation {
        private String firstName;
        private String lastName;
        private String middleName;
        private ContactDTO contactDTO;
        private String reportingManagerName;
        private Long reportingManagerId;
        private AddressDTO communicationAddress;
        private List<Long> departments;
        private Long classSectionId;
        private String className;
        private String sectionName;
        private Long employeeOrgId;
        private Boolean activeStatus;
        @JsonFormat(pattern = "dd-MM-yyyy")
        private Date dateOfJoining;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalInformation {
        private String gender;
        private String bloodGroup;
        @JsonFormat(pattern = "dd-MM-yyyy")
        private Date dob;
        private AddressDTO homeAddress;
        private String guardianName;
        private String guardianRelation;
        private String guardianMobileNo;
    }

}
