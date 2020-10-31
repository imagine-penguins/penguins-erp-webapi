package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.UserControllerImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component("userMapperImpl")
public class UserMapperImpl {

    @Autowired
    PrivilegeService privilegeService;

    @Autowired
    private UserProfileMapperImpl userProfileMapper;

    @Autowired
    private ContactMapperImpl contactMapper;

    public void userToUserCreationDTO(UserCreationDTO userCreationDTO, User user) {
        if ( user == null || userCreationDTO == null) {
            return ;
        }

//        UserCreationDTO userCreationDTO = new UserCreationDTO();

        userCreationDTO.setPrivileges( userPrivilegesToPrivileges( user.getUserPrivileges() ) );
        userCreationDTO.setProfile( userProfileMapper.userProfileToDTO( user.getUserProfile() ) );
        userCreationDTO.setUsername( user.getUsername() );
        userCreationDTO.setPassword( user.getPassword() );
        userCreationDTO.setAdmin( user.getAdmin() );
        userCreationDTO.setSuperAdmin( user.getSuperAdmin() );
        userCreationDTO.setId( user.getId() );
        userCreationDTO.setUserType( user.getUserType() );

//        return userCreationDTO;
    }

    public void userCreationDTOToUser(User user, UserCreationDTO dto) {
        if ( dto == null ) {
            return ;
        }

//        UserDTO user = new UserDTO();

        user.setUserProfile( userProfileMapper.dtoToUserProfile( dto.getProfile() ) );
        user.setId( dto.getId() );
        user.setUsername( dto.getUsername() );
        user.setUserType( dto.getUserType() );
        user.setAdmin( dto.getAdmin() );
        user.setSuperAdmin( dto.getSuperAdmin() );
        user.setPassword( dto.getPassword() );

        userPrivileges( dto, user );

//        return user;
    }

    public List<Integer> userPrivilegesToPrivileges(List<UserPrivilege> userPrivilegeList){
        return userPrivilegeList.stream()
                .map(userPrivilege -> userPrivilege.getDepartmentPrivilege().getPrivilege().getId())
                .collect(Collectors.toList());
    }

    public void userPrivileges(UserCreationDTO dto, User user){
        user.getUserProfile().setUser(user);
        user.setUserPrivileges(dto.getPrivileges()
        .stream()
        .map(id -> new UserPrivilege())
        .collect(Collectors.toList()));
    }

    public UserListDTO.UserDTO userToUserDTO(User entity) {
        UserListDTO.UserDTO dto = new UserListDTO.UserDTO();
        if (entity == null) {
            return null;
        }
        dto.setActive(entity.getActive());
        dto.setContact(contactMapper.contactToContactDTO(entity.getUserProfile().getContact()));
        dto.setFirstName(entity.getUserProfile().getFirstName());
        dto.setLastName(entity.getUserProfile().getLastName());
        dto.setId(entity.getId());
        dto.setProfilePic(entity.getUserProfile().getProfilePic());
        dto.setUserType(entity.getUserType());

        dto.add(
                linkTo(methodOn(UserControllerImpl.class)
                        .updateActiveStatus(dto.getId(), !dto.getActive()))
                        .withRel("update-active-status"));
        dto.add(
                linkTo(methodOn(UserControllerImpl.class)
                        .one(dto.getId()))
                        .withRel("profile"));
        return dto;
    }

}
