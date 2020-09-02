package com.knackitsolutions.crm.imaginepenguins.dbservice;

import static org.assertj.core.api.Assertions.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.AddressMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ContactMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserProfileMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AddressDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ContactDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserProfileDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Address;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Contact;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class DbServiceApplicationTests {

//	@TestConfiguration
//	static class EmoloyeeTestConfiguration{
//
//		@Bean
//		public ModelMapper modelMapper(){
//			return new ModelMapper();
//		}
//	}

	@Test
	void contextLoads() {
	}

	@Autowired
	UserProfileMapper userProfileMapper;

	@Autowired
	AddressMapperImpl addressMapperImpl;

	@Autowired
	ContactMapperImpl contactMapperImpl;


	@Test
	void mapAddressEntityToDTOTest(){
		Address address = new Address("74", "najafgarh", "Delhi", "India", "110045");
		AddressDTO addressDTO = addressMapperImpl.addressToAddressDTO(address);
		assertThat(address.getAddressLine1().equals(addressDTO.getLine1()));
		assertThat(address.getAddressLine2().equals(addressDTO.getLine1()));
		assertThat(address.getCountry().equals(addressDTO.getCountry()));

	}

	@Test
	void mapContactEntityToDTOTest(){
		Contact contact = new Contact("982", "mj@g.co");
     	ContactDTO dto = contactMapperImpl.contactToContactDTO(contact);
     	assertThat(contact.getPhone().equals(dto.getPhone()));
     	assertThat(contact.getEmail().equals(dto.getEmail()));

	}

	@Test
	void mapUserProfileToDTOTest(){
		Address address = new Address("74", "najafgarh", "Delhi", "India", "110045");
		Contact contact = new Contact("982", "mj@g.co");
		UserProfile userProfile = new UserProfile("mayank", "kumar", address, contact);
		UserProfileDTO userProfileDTO = userProfileMapper.userProfileToDTO(userProfile);
		assertThat(userProfile.getAddress().getAddressLine1().equals(userProfileDTO.getAddress().getLine1()));
		assertThat(userProfile.getAddress().getAddressLine2().equals(userProfileDTO.getAddress().getLine2()));
		assertThat(address.getCountry().equals(userProfileDTO.getAddress().getCountry()));
		assertThat(userProfile.getFirstName().equals(userProfileDTO.getFirstName()));
		assertThat(userProfile.getLastName().equals(userProfileDTO.getLastName()));
	}
	

}
