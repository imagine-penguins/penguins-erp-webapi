package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserDocumentType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.AddressMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ContactMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.common.filter.DataFilter;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.InstituteNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.UserFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteClassSectionRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserProfileRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.SearchCriteria;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.specification.UserSpecification;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.document.AmazonDocumentStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/users")
@Validated
@Slf4j
public class UserControllerImpl {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AmazonDocumentStorageClient storageClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactMapperImpl contactMapper;

    @Autowired
    private AddressMapperImpl addressMapper;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private InstituteClassSectionRepository classSectionRepository;

    @Autowired
    private UserDepartmentRepository userDepartmentRepository;

    @Autowired
    private InstituteService instituteService;

    @Autowired
    private StudentService studentService;

    @Autowired
    @Qualifier("userMapperImpl")
    UserMapperImpl userMapper;

    @GetMapping("/{id}")
    public EntityModel<Map<String, Object>> one(@PathVariable("id") Long id){
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserLoginResponseDTO dto = userFacade.findById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", user.getUserProfile().getFirstName());
        map.put("lastName", user.getUserProfile().getLastName());
        map.put("userId", user.getId());
        return EntityModel.of(map);
    }

    @PutMapping("/{userId}/upload")
    public ResponseEntity<String> uploadDoc(@RequestParam("file") MultipartFile multipartFile
            , @PathVariable Long userId, @RequestParam("doc-type") UserDocumentType userDocumentType) throws URISyntaxException {
        String fileName = storageClient.storeFile(multipartFile
                , userService
                        .findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId))
                , userDocumentType);
        return ResponseEntity.created(new URI(fileName)).body("success!!");
    }

    @GetMapping("/{userId}/download")
    public ResponseEntity<Resource> downloadDoc(@PathVariable Long userId
            , @RequestParam("doc-type") UserDocumentType userDocumentType
            , HttpServletRequest request) {
        String fileName = storageClient.getDocumentName(userId, userDocumentType);
        Resource resource = null;
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            resource = storageClient.loadFileAsResource(fileName);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return ResponseEntity.notFound().build();
        }
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ioException) {
            log.error("Could not determine the file type.");
            log.error("IGNORE. " + ioException.getMessage(), ioException);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filepath=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PostMapping("/{userType}")
    public ResponseEntity<String> newUser(@PathVariable("userType") UserType userType, @RequestBody ProfileDTO dto){
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        User newUser = null;
        if (userType == UserType.STUDENT) {
            Optional<InstituteClassSection> classSection = classSectionRepository.findById(
                    dto.getGeneralInformation().getClassSectionId()
            );
            Student newStudent = new Student();
            newStudent.setRollNumber(dto.getGeneralInformation().getRollNumber());
            classSection.ifPresent(cs -> cs.setStudent(newStudent));
            newUser = newStudent;
        } else if (userType == UserType.EMPLOYEE) {
            Employee newEmployee = new Employee();
            newEmployee.setEmployeeOrgId(dto.getGeneralInformation().getEmployeeOrgId());
            instituteService
                    .findById(userContext.getInstituteId())
                    .ifPresent(institute -> institute.setEmployee(newEmployee));
            Optional<Employee> manager = employeeService.findById(dto.getGeneralInformation().getReportingManagerId());
            manager.ifPresent(m -> m.setSubordinates(newEmployee));
            newUser = newEmployee;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UserType Selected. Valid values are S -> Student, E -> Employee");
        }

        UserProfile newUserProfile = userMapper.dtoToEntity(dto);

        newUserProfile = userProfileRepository.save(newUserProfile);

        newUser.setActive(dto.getGeneralInformation().getActiveStatus());
        newUser.setUsername(userService.generateUsername(newUserProfile.getContact().getEmail(), newUserProfile.getId()));
        newUser.setPassword(userService.generateRandomPassword(newUserProfile.getContact().getEmail()));
        newUser.setUserType(userType);

        newUser.setUserProfile(newUserProfile);


        List<UserDepartment> userDepartments = userDepartmentRepository.findAllById(dto.getGeneralInformation().getDepartments());

        newUser.setUserDepartments(userDepartments);

        if (userType == UserType.EMPLOYEE) {
            employeeService.newEmployee((Employee) newUser);
        } else if (userType == UserType.STUDENT) {
            studentService.save((Student) newUser);
        }

        return ResponseEntity.ok("Successfully Created New User.");
    }

    /*

    @Override
    public ResponseEntity<?> replaceUser(UserDTO newUser, Long id) {
        UserDTO replacedUser = userRepository.findById(id)
                .map(user -> {
                    user.setUserType(newUser.getUserType());
                    user.setAdmin(newUser.getAdmin());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
        EntityModel<UserDTO> entityModel = userModelAssembler.toModel(replacedUser);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        userRepository.delete(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id)));
        return ResponseEntity.noContent().build();
    }
*/

    @GetMapping
    public EntityModel<ProfileDTO> myProfile() {
        UserContext userContext = (UserContext)authenticationFacade.getAuthentication().getPrincipal();
        User user = userService
                .findById(userContext.getUserId()).orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        ProfileDTO profileDTO = userMapper.entityToDTO(user);
        return EntityModel.of(profileDTO);
    }

    @GetMapping("/{userId}")
    public EntityModel<ProfileDTO> myProfile(@PathVariable Long userId) {
        UserContext userContext = (UserContext)authenticationFacade.getAuthentication().getPrincipal();
        User user = userService
                .findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ProfileDTO profileDTO = userMapper.entityToDTO(user);
        return EntityModel.of(profileDTO);
    }

    @GetMapping("/dir")
    public PagedModel<UserListDTO.UserDTO> all(@RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
            , @RequestParam(required = false) String[] search
            , @RequestParam(defaultValue = "id,desc") String[] sort){

        UserContext userContext = (UserContext)authenticationFacade.getAuthentication().getPrincipal();
        Map<String, List<SearchCriteria>> searchMap = FilterService.createSearchMap(search);
        if ( !(userContext.getUserType() == UserType.EMPLOYEE) )
            throw new RuntimeException("User is not employee hence not permitted here.");

        Pageable pageable = PageRequest.of(page, size, SortingService.sort(sort));
        Specification<User> userSpecification = null;
        try {
            userSpecification = UserSpecification.filterUsers(searchMap);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse date from search parameters.");
        }

        Page<User> all = userRepository.findAll(userSpecification, pageable);
        List<UserListDTO.UserDTO> collect = all
                .stream()
                .map(userMapper::userToUserDTO)
                .map(dto -> dto.add(
                        linkTo(methodOn(UserControllerImpl.class)
                                .updateActiveStatus(dto.getId(), !dto.getActive()))
                                .withRel("update-active-status")))
                .map(dto -> dto.add(
                        linkTo(methodOn(UserControllerImpl.class)
                                .one(dto.getId()))
                                .withRel("profile")))
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, all.getTotalElements(), all.getTotalPages());
        PagedModel<UserListDTO.UserDTO> userDTOS = PagedModel.of(collect, pageMetadata);

        return userDTOS;
    }

    @PutMapping(value = "/{userId}/{status}")
    public ResponseEntity<EntityModel<String>> updateActiveStatus(@PathVariable(value = "userId") Long userId
            , @PathVariable(value = "status") Boolean active) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (active == user.getActive())
            return ResponseEntity.badRequest().body(EntityModel.of("Status is already " +  active));

        user.setActive(active);
        User updatedUser = userService.save(user);
        if (updatedUser.getActive() == active) {
            return ResponseEntity
                    .created(linkTo(methodOn(UserControllerImpl.class).updateActiveStatus(userId, active)).toUri())
                    .body(
                            EntityModel
                                    .of("Status Successfully Updated")
                                    .add(
                                            linkTo(methodOn(UserControllerImpl.class).updateActiveStatus(userId, !active))
                                                    .withRel("update-active-status")
                                    )
                    );
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/hierarchy")
    public ResponseEntity<EntityModel<EmployeeHierarchy>> hierarchy(@RequestParam Optional<Long> userId) {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication().getPrincipal();
        User user = userService.findById(userContext.getUserId()).orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));
        log.debug("User type is: {}", user.getUserType());

        if ( !(user instanceof Employee)) {
            throw new RuntimeException("User is not employee");
        }

        Employee employee = userId
                .map(aLong -> employeeService.findById(aLong).orElseThrow(() -> new UserNotFoundException(userId.get())))
                .orElse((Employee)user);

        EntityModel<EmployeeHierarchy> manager = EntityModel
                .of(new EmployeeHierarchy(employee));

        Optional<Employee> managersManager = Optional.ofNullable(employee.getManager());

        managersManager.ifPresent(m -> manager
                .add(
                        linkTo(methodOn(UserControllerImpl.class)
                                .hierarchy(Optional.of(m.getId()))).withRel("manager")
                )
        );

        return ResponseEntity.ok(manager);
    }

    @GetMapping("/institutes")
    public CollectionModel<EntityModel<InstituteDTO>> myInstitute() {
        UserContext userContext = (UserContext) authenticationFacade.getAuthentication();
        List<InstituteDTO> instituteDTOS = userFacade.getInstitutes(userContext.getUserId());
        if (instituteDTOS.size() == 0) {
            log.info("No institutes found for id: {}", userContext.getUserId());
            return null;
        }
        List<EntityModel<InstituteDTO>> institutes = instituteDTOS.stream().map(instituteDTO -> {
            log.info("InstitutesDTO: {}", instituteDTO);
            return EntityModel.of(instituteDTO, linkTo(methodOn(InstituteController.class)
                    .one(instituteDTO.getId()))
                    .withSelfRel());
        }).collect(Collectors.toList());

        return CollectionModel.of(institutes,
                linkTo(methodOn(InstituteController.class).all()).withRel("institutes"));

    }
}
