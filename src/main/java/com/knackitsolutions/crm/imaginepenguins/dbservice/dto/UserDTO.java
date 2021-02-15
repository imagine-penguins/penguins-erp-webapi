package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

public interface UserDTO {
    Long getUserId();
    String getFirstName();
    String getLastName();
    String getMiddleName();
    String getProfilePic();
    String getPassportPic();

    String setUserId(Long userId);

    String setFirstName(String firstName);

    String setLastName(String lastName);

    String setMiddleName(String middleName);

    String setProfilePic(String profilePic);

    String setPassportPic(String passportPic);
}
