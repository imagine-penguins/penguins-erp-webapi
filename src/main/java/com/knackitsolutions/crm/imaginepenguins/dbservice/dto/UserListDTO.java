package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO extends RepresentationModel<UserListDTO> {

    private List<UserDTO> userDTOS;
    private Integer totalUsers;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO extends RepresentationModel<UserDTO> {
        String profilePic;
        String firstName;
        String lastName;
        Long id;
        ContactDTO contact;
        UserType userType;
        Boolean active;

    }
}
