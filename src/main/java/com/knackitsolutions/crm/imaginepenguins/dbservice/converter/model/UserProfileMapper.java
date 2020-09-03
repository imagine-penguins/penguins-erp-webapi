package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserProfileDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserProfile;

import java.util.List;

//@Mapper(componentModel = "spring", uses = {
//        AddressMapper.class, ContactMapper.class
//})
public interface UserProfileMapper {
    UserProfileDTO userProfileToDTO(UserProfile entity);
    UserProfile dtoToUserProfile(UserProfileDTO dto);

}
