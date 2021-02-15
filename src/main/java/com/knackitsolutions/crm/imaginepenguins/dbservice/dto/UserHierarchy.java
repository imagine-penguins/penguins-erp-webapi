package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import lombok.Value;

@Value
@JsonPropertyOrder({"firstName", "lastName", "middleName", "email", "phone", "designation", "userId", "profilePic", "managerId"})
public class UserHierarchy {

    @JsonIgnore
    private final User user;
    @JsonIgnore
    private final UserDocumentStore userDocumentStore;

    public String getFirstName() { return this.user.getUserProfile().getFirstName(); }

    public String getMiddleName() {
        return this.user.getUserProfile().getMiddleName();
    }

    public String getLastName() {
        return this.user.getUserProfile().getFirstName();
    }

    public String getEmail() {
        return  this.user.getUserProfile().getContact().getEmail();
    }

    public String getPhone() {
        return this.user.getUserProfile().getContact().getPhone();
    }

    public String getDesignation() {
        return ((Employee)this.user).getDesignation();
    }

    public Long getUserId() { return this.user.getId(); }

    public String getProfilePic() { return this.userDocumentStore.getStoreURL(); }

    public Long getManagerId(){
        return ((Employee)this.user).getManager().getId(); }

}
