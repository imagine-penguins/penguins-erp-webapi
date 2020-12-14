package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserProfileDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserProfileMapperImpl {

    @Autowired
    private AddressMapperImpl addressMapper;
    @Autowired
    private ContactMapperImpl contactMapper;

    public UserProfileDTO userProfileToDTO(UserProfile entity) {
        if ( entity == null ) {
            return null;
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO();

        userProfileDTO.setFirstName( entity.getFirstName() );
        userProfileDTO.setLastName( entity.getLastName() );
        userProfileDTO.setContact( contactMapper.contactToContactDTO( entity.getContact() ) );
        userProfileDTO.setAddress( addressMapper.addressToAddressDTO( entity.getPersonalAddress() ) );

        return userProfileDTO;
    }

    public UserProfile dtoToUserProfile(UserProfileDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName( dto.getFirstName() );
        userProfile.setLastName( dto.getLastName() );
        userProfile.setPersonalAddress( addressMapper.addressDTOToAddress( dto.getAddress() ) );
        userProfile.setContact( contactMapper.contactDTOtoContact( dto.getContact() ) );

        return userProfile;
    }
}
