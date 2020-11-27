package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.UserControllerImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.PrivilegeService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component("userMapperImpl")
@RequiredArgsConstructor
public class UserMapperImpl {

    private final UserProfileMapperImpl userProfileMapper;

    private final ContactMapperImpl contactMapper;

    public void userToUserCreationDTO(UserCreationDTO userCreationDTO, User user) {
        if ( user == null || userCreationDTO == null) {
            return ;
        }

        userCreationDTO.setPrivileges( userPrivilegesToPrivileges( user.getUserPrivileges() ) );
        userCreationDTO.setProfile( userProfileMapper.userProfileToDTO( user.getUserProfile() ) );
        userCreationDTO.setUsername( user.getUsername() );
        userCreationDTO.setPassword( user.getPassword() );
        userCreationDTO.setAdmin( user.getAdmin() );
        userCreationDTO.setSuperAdmin( user.getSuperAdmin() );
        userCreationDTO.setId( user.getId() );
        userCreationDTO.setUserType( user.getUserType() );

    }

    public void userCreationDTOToUser(User user, UserCreationDTO dto) {
        if ( dto == null ) {
            return ;
        }
        user.setUserProfile( userProfileMapper.dtoToUserProfile( dto.getProfile() ) );
        user.setId( dto.getId() );
        user.setUsername( dto.getUsername() );
        user.setUserType( dto.getUserType() );
        user.setAdmin( dto.getAdmin() );
        user.setSuperAdmin( dto.getSuperAdmin() );
        user.setPassword( dto.getPassword() );

        userPrivileges( dto, user );

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
        dto.setProfilePic(entity
                .getUserDocumentStores()
                .stream()
                .filter(userDocumentStore -> userDocumentStore.getDocumentType() == UserDocumentType.DISPLAY_PICTURE)
                .findFirst()
                .orElseGet(UserDocumentStore::new)
                .getFileName()
        );
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
