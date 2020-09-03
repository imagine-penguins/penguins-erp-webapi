package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstituteMapper {

    @Autowired
    AddressMapperImpl addressMapper;

    @Autowired
    ContactMapperImpl contactMapper;

    public void entityToDTO(InstituteDTO dto, Institute entity){
        if (entity == null || dto == null)
            return;
        dto.setId(entity.getId());
        dto.setAddress(addressMapper.addressToAddressDTO(entity.getAddress()));
        dto.setContact(contactMapper.contactToContactDTO(entity.getContact()));
        dto.setCloseTime(entity.getCloseTime());
        dto.setOpenTime(entity.getOpenTime());
        dto.setInstituteType(entity.getInstituteType());
        dto.setLogoImg(entity.getLogoImg());

    }

    public InstituteDTO entityToDTO(Institute entity){
        if (entity == null)
            return null;
        InstituteDTO dto = new InstituteDTO();
        dto.setId(entity.getId());
        dto.setAddress(addressMapper.addressToAddressDTO(entity.getAddress()));
        dto.setContact(contactMapper.contactToContactDTO(entity.getContact()));
        dto.setCloseTime(entity.getCloseTime());
        dto.setOpenTime(entity.getOpenTime());
        dto.setInstituteType(entity.getInstituteType());
        dto.setLogoImg(entity.getLogoImg());
        return dto;
    }


}
