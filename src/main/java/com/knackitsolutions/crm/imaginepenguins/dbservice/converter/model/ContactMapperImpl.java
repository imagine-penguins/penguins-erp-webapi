package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ContactDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Contact;
import org.springframework.stereotype.Component;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-08-24T15:35:13+0530",
    comments = "version: 1.3.0.Beta2, compiler: javac, environment: Java 1.8.0_201 (Oracle Corporation)"
)
@Component
public class ContactMapperImpl /*implements ContactMapper*/ {

    public ContactDTO contactToContactDTO(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setPhone( contact.getPhone() );
        contactDTO.setEmail( contact.getEmail() );
        contactDTO.setAlternatePhone( contact.getAlternatePhone() );
        contactDTO.setAlternateEmail( contact.getAlternateEmail() );

        return contactDTO;
    }

    public Contact contactDTOtoContact(ContactDTO contactDTO) {
        if ( contactDTO == null ) {
            return null;
        }

        Contact contact = new Contact();
        contact.setAlternateEmail( contactDTO.getAlternateEmail() );
        contact.setAlternatePhone( contactDTO.getAlternatePhone() );
        contact.setEmail( contactDTO.getEmail() );
        contact.setPhone( contactDTO.getPhone() );

        return contact;
    }
}
