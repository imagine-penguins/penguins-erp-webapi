package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.common.sort.GroupBySorter;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.common.filter.DataFilter;
import com.knackitsolutions.crm.imaginepenguins.dbservice.common.sort.DataSort;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.UserFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/users")
@Validated
@Slf4j
public class UserControllerImpl {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    UserFacade userFacade;

    @Autowired
    UserService userService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    @Qualifier("userMapperImpl")
    UserMapperImpl userMapper;

    @GetMapping("/{id}")
    public EntityModel<UserLoginResponseDTO> one(@PathVariable("id") Long id){
        UserLoginResponseDTO dto = userFacade.findById(id);
        return EntityModel.of(dto); //userLoginModelAssembler.toModel(userFacade.findById(id));
    }
/*
    @Override
    public ResponseEntity<?> newUser(UserDTO user) {
        user.getUserProfile().setUser(user);
        log.info("UserDTO: {}", user);
        log.info("UserDTO Employee: {}", user.getEmployee());
        log.info("UserDTO Type: {}", user.getUserType());
        if (user.getUserType() == UserType.EMPLOYEE) {
            log.info("Setting UserDTO in Employee");
            log.info("Employee: {}", user.getEmployee());
            log.info("Employee's user: {}", user.getEmployee().getUser());
            log.info("UserDTO: {}", user);
            user.getEmployee().setUser(user);
        }
        EntityModel<UserDTO> entityModel = userModelAssembler
                .toModel(userRepository.save(user));

        return ResponseEntity.created(
                entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()
        ).body(entityModel);
    }

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
    }*/


    @GetMapping
    public UserListDTO all(@RequestParam(defaultValue = "0") @Min(0) int page
            , @RequestParam(defaultValue = "10") @Min(1) int size
            , DataFilter dataFilter
            , @RequestParam(defaultValue = "id,desc") String[] sort){
//            , @RequestParam(name = "userType") Optional<UserType> userType
//            , @RequestParam(name = "userRole") Optional<PrivilegeCode> privilegeCode
//            , @RequestParam(name = "active") Optional<Boolean> active
//            , @RequestParam(name = "sort", defaultValue = "name") SortField sortField
//            , @RequestParam(name = "order", defaultValue = "asc") SortOrder sortOrder) {

        UserContext userContext = (UserContext)authenticationFacade.getAuthentication().getPrincipal();
        User user = userService.findById(userContext.getUserId()).orElseThrow(() -> new UserNotFoundException(userContext.getUserId()));

        if ( !(user instanceof Employee) )
            throw new RuntimeException("User is not employee hence not permitted here.");

        Employee employee = (Employee) user;

        List<User> users = userFacade.getUsers(employee, employee.getInstitute().getId()
                , dataFilter.getActive(), dataFilter.getUserTypes());

        List<UserListDTO.UserDTO> userDTOS = new ArrayList<>();
//        users
//                .stream()
//                .map(userMapper::userToUserDTO)
//                .sorted(
//                        dataSort
//                                .getSortOrder()
//                                .order(
//                                        new GroupBySorter(
//                                                dataSort
//                                                        .getSortFields()
//                                                        .stream()
//                                                        .map(
//                                                                sortField -> sortField.comparator()
//                                                        )
//                                                        .collect(Collectors.toList())
//                                        )
//                                )
//                )
//                .skip(page * size)
//                .limit(size)
//                .forEach(userDTOS::add);

        UserListDTO userListDTO = userFacade.newUserListDTO(userDTOS, page, size, users.size());

        userListDTO.add(linkTo(methodOn(UserControllerImpl.class)
                .all(page, size, dataFilter, sort)).withSelfRel());

        userListDTO.add(linkTo(methodOn(UserControllerImpl.class)
                .all(page + 1, size, dataFilter, sort)).withRel("next-page"));

        if (page > 0)
            userListDTO.add(linkTo(methodOn(UserControllerImpl.class)
                    .all(page - 1, size, dataFilter, sort))
                    .withRel("previous-page"));

        return userListDTO;
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<EntityModel<String>> updateActiveStatus(@PathVariable(value = "userId") Long userId
            , @RequestParam(name = "active") Boolean active) {
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

    @GetMapping("/institute")
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
