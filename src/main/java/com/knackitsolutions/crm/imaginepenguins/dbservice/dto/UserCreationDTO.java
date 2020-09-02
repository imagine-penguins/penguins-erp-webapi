package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;

import javax.persistence.Id;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmployeeCreationDTO.class, name = "E"),
        @JsonSubTypes.Type(value = StudentCreationDTO.class, name = "S"),
        @JsonSubTypes.Type(value = ParentCreationDTO.class, name = "P")
}
)
public class UserCreationDTO {

    @Id
    Long id;

    String username;

    String password;

    Boolean isAdmin;

    Boolean isSuperAdmin;

    List<Integer> privileges;

    UserProfileDTO profile;

    UserType userType;

    public UserCreationDTO() {
    }

    public UserCreationDTO(String username, String password){
        this();
        this.username = username;
        this.password = password;
    }

    public UserCreationDTO(String username, String password, Boolean isAdmin, Boolean isSuperAdmin) {
        this(username, password);
        this.isAdmin = isAdmin;
        this.isSuperAdmin = isSuperAdmin;
    }

    public UserCreationDTO(String username, String password, Boolean isAdmin, Boolean isSuperAdmin, List<Integer> privileges, UserProfileDTO profile) {
        this(username, password, isAdmin, isSuperAdmin);
        this.privileges = privileges;
        this.profile = profile;
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

    public List<Integer> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Integer> privileges) {
        this.privileges = privileges;
    }

    public UserProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(UserProfileDTO profile) {
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
