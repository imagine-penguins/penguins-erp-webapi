package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public void entityToDTO(UserLoginResponseDTO userLoginResponseDTO, User user){
        if (user == null || userLoginResponseDTO == null)
            return;
        userLoginResponseDTO.setUserId(user.getId());
    }

    public UserLoginResponseDTO entityToDTO(User user){
        if (user == null)
            return null;

        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
        userLoginResponseDTO.setUserId(user.getId());
        return userLoginResponseDTO;
    }

}
