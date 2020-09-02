package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AddressDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Address;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-08-24T15:35:13+0530",
    comments = "version: 1.3.0.Beta2, compiler: javac, environment: Java 1.8.0_201 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl /*implements AddressMapper */{

    public AddressDTO addressToAddressDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setLine2( address.getAddressLine2() );
        addressDTO.setLine1( address.getAddressLine1() );
        addressDTO.setState( address.getState() );
        addressDTO.setZipcode( address.getZipcode() );
        addressDTO.setCountry( address.getCountry() );

        return addressDTO;
    }

    public Address addressDTOToAddress(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        Address address = new Address();

        address.setAddressLine1( addressDTO.getLine1() );
        address.setAddressLine2( addressDTO.getLine2() );
        address.setState( addressDTO.getState() );
        address.setCountry( addressDTO.getCountry() );
        address.setZipcode( addressDTO.getZipcode() );

        return address;
    }
}
