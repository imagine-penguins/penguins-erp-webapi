package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserProfile;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacade {

    private static final Logger log = LoggerFactory.getLogger(UserFacade.class);

    @Autowired
    UserService userService;

    public UserLoginResponseDTO newUser(){
        return null;
    }

    public List<UserLoginResponseDTO> findAll(){
        return userService.findAll()
                .stream()
                .map(user -> convertToResponseDTO(user))
                .collect(Collectors.toList());
    }

    public UserLoginResponseDTO findById(Long id){
        return convertToResponseDTO(userService.findById(id));
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO requestDTO){
        User user = userService.login(requestDTO.getUsername());
        if (user == null){
            throw new UserNotFoundException(0l);
        }

        if (user.getPassword() != null && user.getPassword().equals(requestDTO.getPassword())){
            return convertToResponseDTO(user);
        }
        else
            throw new UserNotFoundException(0l);
    }

    private UserLoginResponseDTO convertToResponseDTO(User user){
        UserLoginResponseDTO dto = new UserLoginResponseDTO();
        dto.setId(user.getId());
        dto.setAdmin(user.getAdmin());
        dto.setSuperAdmin(user.getSuperAdmin());
        dto.setUsername(user.getUsername());
        dto.setUserType(user.getUserType().getUserTypeValue());
        if (user.getUserPrivileges() != null){
            dto.setPrivileges(user.getUserPrivileges()
                    .stream()
                    .map(i -> new PrivilegeDTO(i.getPrivilege().getId(), i.getPrivilege().getPrivilegeName(), i.getPrivilege().getPrivilegeDesc()))
                    .collect(Collectors.toList()));
        }

        UserProfile profile = user.getUserProfile();
        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            if (profile.getAddress() != null) {
                dto.setAddressLine1(profile.getAddress().getAddressLine1());
                dto.setAddressLine2(profile.getAddress().getAddressLine2());
                dto.setCountry(profile.getAddress().getCountry());
                dto.setState(profile.getAddress().getState());
                dto.setZipcode(profile.getAddress().getZipcode());
            }
            if (profile.getContact() != null) {
                dto.setEmail(profile.getContact().getEmail());
                dto.setAlternateEmail(profile.getContact().getAlternateEmail());
                dto.setPhone(profile.getContact().getPhone());
                dto.setAlternatePhone(profile.getContact().getAlternatePhone());
            }
        }
        return dto;
    }

}
