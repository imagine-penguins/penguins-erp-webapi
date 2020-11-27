package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserLoginResponseMapper {

    public void entityToDTO(UserLoginResponseDTO userLoginResponseDTO, User user){
        if (user == null || userLoginResponseDTO == null)
            return;
    }

    public UserLoginResponseDTO entityToDTO(User user){
        if (user == null)
            return null;

        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
        entityToDTO(userLoginResponseDTO, user);
        return userLoginResponseDTO;
    }

}
