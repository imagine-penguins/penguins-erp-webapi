package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.UserControllerImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ProfileDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.UserDocumentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component("userMapperImpl")
@RequiredArgsConstructor
public class UserMapperImpl {

    private final UserProfileMapperImpl userProfileMapper;

    private final ContactMapperImpl contactMapper;

    private final AddressMapperImpl addressMapper;

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
        dto.setPassportPic(
                entity
                        .getUserDocumentStores()
                        .stream()
                        .filter(userDocumentStore -> userDocumentStore.getDocumentType() == UserDocumentType.PASSPORT_PICTURE)
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

    public void entityToDTO(User entity, ProfileDTO dto) {
        if (entity == null || dto == null) {
            return;
        }
        dto.setUserId(entity.getId());
        dto.setProfilePic(getDocTypeFrom(entity, UserDocumentType.DISPLAY_PICTURE));
        dto.setPassportPic(getDocTypeFrom(entity, UserDocumentType.PASSPORT_PICTURE));

    }
    private String getDocTypeFrom(User entity, UserDocumentType userDocumentType) {
        return entity
                .getUserDocumentStores()
                .stream()
                .filter(userDocumentStore -> userDocumentStore.getDocumentType() == userDocumentType)
                .findFirst()
                .orElseGet(UserDocumentStore::new)
                .getFileName();
    }

    public ProfileDTO entityToDTO(User user) {
        if (user == null) {
            return null;
        }
        ProfileDTO dto = new ProfileDTO();
        entityToDTO(user, dto);
        ProfileDTO.GeneralInformation generalInformation = new ProfileDTO.GeneralInformation();
        generalInformation.setActiveStatus(user.getActive());
        if (user.getUserType() == UserType.STUDENT) {
            Student student = (Student) user;
            generalInformation.setClassSectionId(student.getInstituteClassSection().getId());
            Teacher classTeacher = student.getInstituteClassSection().getTeacher();
            generalInformation.setReportingManagerName(classTeacher.getUserProfile().getFirstName() + " " + classTeacher.getUserProfile().getLastName());
            generalInformation.setReportingManagerId(classTeacher.getId());
            generalInformation.setClassName(student.getInstituteClassSection().getInstituteClass().getClasss().getClassName());
            generalInformation.setSectionName(student.getInstituteClassSection().getSection().getSectionName());

        }
        UserProfile userProfile = user.getUserProfile();
        generalInformation.setContactDTO(contactMapper.contactToContactDTO(userProfile.getContact()));
        generalInformation.setFirstName(userProfile.getFirstName());
        generalInformation.setLastName(userProfile.getLastName());
        generalInformation.setMiddleName(userProfile.getMiddleName());
        generalInformation.setDepartments(user.getUserDepartments().stream().map(userDepartment -> userDepartment.getInstituteDepartment().getId()).collect(Collectors.toList()));
        generalInformation.setCommunicationAddress(addressMapper.addressToAddressDTO(userProfile.getCommunicationAddress()));
        if (user.getUserType() == UserType.EMPLOYEE) {
            Employee employee = (Employee) user;
            Employee manager = employee.getManager();
            generalInformation.setReportingManagerId(manager.getId());
            generalInformation.setReportingManagerName(manager.getUserProfile().getFirstName() + " " + manager.getUserProfile().getLastName());
        }
        generalInformation.setDateOfJoining(userProfile.getDateOfJoining());
        dto.setGeneralInformation(generalInformation);

        ProfileDTO.PersonalInformation personalInformation = new ProfileDTO.PersonalInformation();
        personalInformation.setDob(userProfile.getDob());
        personalInformation.setBloodGroup(userProfile.getBloodGroup());
        personalInformation.setGender(userProfile.getGender());
        personalInformation.setHomeAddress(addressMapper.addressToAddressDTO(userProfile.getPersonalAddress()));
        personalInformation.setGuardianName(userProfile.getGuardianName());
        personalInformation.setGuardianRelation(userProfile.getGuardianRelation());
        personalInformation.setGuardianMobileNo(userProfile.getGuardianPhoneNo());
        dto.setPersonalInformation(personalInformation);

        return dto;
    }

    public User entityToDTO(UserType userType, ProfileDTO dto) {
        User user = new User();
        if (dto == null)
            return user;
        ProfileDTO.GeneralInformation generalInformation = dto.getGeneralInformation();
        user.setActive(generalInformation.getActiveStatus());
        user.setUserType(userType);
        //TODO
        return user;
    }

}
