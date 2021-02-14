package com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ProfileDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserListDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
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
        dto.setUserType(entity.getUserType());
        return dto;
    }

    public void entityToDTO(User entity, ProfileDTO dto) {
        if (entity == null || dto == null) {
            return;
        }
        dto.setUserId(entity.getId());
        dto.setUserType(entity.getUserType());
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
            generalInformation.setRollNumber(student.getRollNumber());
        }
        UserProfile userProfile = user.getUserProfile();
        generalInformation.setContactDTO(contactMapper.contactToContactDTO(userProfile.getContact()));
        generalInformation.setFirstName(userProfile.getFirstName());
        generalInformation.setLastName(userProfile.getLastName());
        generalInformation.setMiddleName(userProfile.getMiddleName());
        generalInformation
                .setDepartments(
                        user
                                .getUserDepartments()
                                .stream()
                                .map(userDepartment -> userDepartment.getInstituteDepartment().getId())
                                .collect(Collectors.toList())
                );
        generalInformation.setCommunicationAddress(addressMapper.addressToAddressDTO(userProfile.getCommunicationAddress()));
        if (user.getUserType() == UserType.EMPLOYEE) {
            Employee employee = (Employee) user;
            Employee manager = employee.getManager();
            generalInformation.setReportingManagerId(manager.getId());
            generalInformation
                    .setReportingManagerName(manager.getUserProfile().getFirstName() + " " + manager.getUserProfile().getLastName());
            generalInformation.setDesignation(employee.getDesignation());
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
        return user;
    }

    public UserProfile dtoToEntity(ProfileDTO dto) {
        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setContact(contactMapper.contactDTOtoContact(dto.getGeneralInformation().getContactDTO()));
        newUserProfile.setPersonalAddress(addressMapper.addressDTOToAddress(dto.getPersonalInformation().getHomeAddress()));
        newUserProfile
                .setCommunicationAddress(addressMapper
                        .addressDTOToAddress(dto.getGeneralInformation().getCommunicationAddress()));
        newUserProfile.setFirstName(dto.getGeneralInformation().getFirstName());
        newUserProfile.setLastName(dto.getGeneralInformation().getLastName());
        newUserProfile.setMiddleName(dto.getGeneralInformation().getMiddleName());
        newUserProfile.setBloodGroup(dto.getPersonalInformation().getBloodGroup());
        newUserProfile.setDob(dto.getPersonalInformation().getDob());
        newUserProfile.setGender(dto.getPersonalInformation().getGender());
        newUserProfile.setGuardianName(dto.getPersonalInformation().getGuardianName());
        newUserProfile.setGuardianPhoneNo(dto.getPersonalInformation().getGuardianMobileNo());
        newUserProfile.setGuardianRelation(dto.getPersonalInformation().getGuardianRelation());
        return newUserProfile;
    }
}
