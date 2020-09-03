package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserControllerImpl {

    private static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);

    @Autowired
    UserFacade userFacade;

    @Autowired
    UserLoginModelAssembler userLoginModelAssembler;

    @Autowired
    TeacherModelAssembler teacherModelAssembler;

    @Autowired
    StudentModelAssembler studentModelAssembler;

    @Autowired
    ParentModelAssembler parentModelAssembler;

    @Autowired
    EmployeeModelAssembler employeeModelAssembler;

    @GetMapping
    public CollectionModel<EntityModel<UserLoginResponseDTO>> all() {
        List<EntityModel<UserLoginResponseDTO>> userList = userFacade.findAll()
                .stream()
                .map(userLoginModelAssembler::toModel)
                .collect(Collectors.toList());
        return  CollectionModel.of(userList, linkTo(methodOn(UserControllerImpl.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserLoginResponseDTO> one(@PathVariable("id") Long id) {
        return userLoginModelAssembler.toModel(userFacade.findById(id));
    }
/*
    @Override
    public ResponseEntity<?> newUser(User user) {
        user.getUserProfile().setUser(user);
        log.info("User: {}", user);
        log.info("User Employee: {}", user.getEmployee());
        log.info("User Type: {}", user.getUserType());
        if (user.getUserType() == UserType.EMPLOYEE) {
            log.info("Setting User in Employee");
            log.info("Employee: {}", user.getEmployee());
            log.info("Employee's user: {}", user.getEmployee().getUser());
            log.info("User: {}", user);
            user.getEmployee().setUser(user);
        }
        EntityModel<User> entityModel = userModelAssembler
                .toModel(userRepository.save(user));

        return ResponseEntity.created(
                entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()
        ).body(entityModel);
    }

    @Override
    public ResponseEntity<?> replaceUser(User newUser, Long id) {
        User replacedUser = userRepository.findById(id)
                .map(user -> {
                    user.setUserType(newUser.getUserType());
                    user.setAdmin(newUser.getAdmin());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
        EntityModel<User> entityModel = userModelAssembler.toModel(replacedUser);

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO requestDTO){
        UserLoginResponseDTO dto = userFacade.login(requestDTO);
//        if (dto instanceof EmployeeLoginResponseDTO){
//            EmployeeLoginResponseDTO empDTO = (EmployeeLoginResponseDTO)dto;
//            if (empDTO instanceof TeacherLoginResponseDTO)
//                return ResponseEntity.ok(teacherModelAssembler.toModel((TeacherLoginResponseDTO)dto));
//            else
//                return ResponseEntity.ok(employeeModelAssembler.toModel(empDTO));
//        }
//        else if (dto instanceof ParentLoginResponseDTO)
//            return ResponseEntity.ok(parentModelAssembler.toModel((ParentLoginResponseDTO)dto));
//        else if (dto instanceof StudentLoginResponseDTO)
//            return ResponseEntity.ok(studentModelAssembler.toModel((StudentLoginResponseDTO)dto));

        //Change it later

        return ResponseEntity.ok(EntityModel.of(dto,
                linkTo(methodOn(UserControllerImpl.class).one(dto.getUserId())).withSelfRel(),
                linkTo(methodOn(UserControllerImpl.class).all()).withRel("users"),
                linkTo(methodOn(DashboardController.class).webDashboardDTO(dto.getUserId(), 1l)).withRel("web-dashboard"),
                linkTo(methodOn(DashboardController.class).appDashboardDTO(dto.getUserId(), 1l)).withRel("app-dashboard"),
                linkTo(methodOn(UserControllerImpl.class).institute(dto.getUserId())).withRel("institute")
        ));
    }

    @GetMapping("/{id}/institute")
    public CollectionModel<EntityModel<InstituteDTO>> institute(@PathVariable("id") Long id){
        List<InstituteDTO> instituteDTOS = userFacade.getInstitutes(id);
        if (instituteDTOS.size() == 0) {
            log.info("No institutes found for id: {}", id);
            return null;
        }
        List<EntityModel<InstituteDTO>> institutes = instituteDTOS.stream().map(instituteDTO -> {
            log.info("InstitutesDTO: {}", instituteDTO);
            instituteDTO.add(linkTo(methodOn(InstituteControllerImpl.class)
                    .one(instituteDTO.getId()))
                    .withSelfRel());
            instituteDTO.add(linkTo(methodOn(InstituteControllerImpl.class)
                    .allBranches(instituteDTO.getId()))
                    .withRel("branches"));
            return EntityModel.of(instituteDTO);}).collect(Collectors.toList());

        return CollectionModel.of(institutes,
                linkTo(methodOn(InstituteControllerImpl.class).all()).withRel("institutes"));

    }
}
