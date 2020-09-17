package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.assembler.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.UserFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserDepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    UserDepartmentRepository userDepartmentRepository;

    @Autowired
    PrivilegeMapper privilegeMapper;

    @GetMapping
    public CollectionModel<EntityModel<UserLoginResponseDTO>> all() {
        List<EntityModel<UserLoginResponseDTO>> userList = userFacade.findAll()
                .stream()
                .map(user -> EntityModel.of(user, loginLinks(user)))
                .collect(Collectors.toList());
        return  CollectionModel.of(userList, linkTo(methodOn(UserControllerImpl.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserLoginResponseDTO> one(@PathVariable("id") Long id) {
        UserLoginResponseDTO dto = userFacade.findById(id);
        return EntityModel.of(dto, loginLinks(dto)); //userLoginModelAssembler.toModel(userFacade.findById(id));
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
        UserLoginResponseDTO dto = userFacade.authLogin(requestDTO);
        return ResponseEntity.ok(EntityModel.of(dto, loginLinks(dto)));
    }

    private List<Link> loginLinks(UserLoginResponseDTO dto){
        List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(dto.getUserId());
        Long departmentId = userDepartments.get(0).getInstituteDepartment().getId();
        return Stream.of(linkTo(methodOn(UserControllerImpl.class).one(dto.getUserId())).withSelfRel(),
                linkTo(methodOn(UserControllerImpl.class).all()).withRel("users"),
                linkTo(methodOn(DashboardController.class).webDashboardDTO(
                        dto.getUserId(), departmentId))
                        .withRel("web-dashboard"),
                linkTo(methodOn(DashboardController.class)
                        .appDashboardDTO(dto.getUserId(), departmentId)).withRel("app-dashboard"),
                linkTo(methodOn(UserControllerImpl.class).institute(dto.getUserId())).withRel("institute"),
                linkTo(methodOn(UserControllerImpl.class).departments(dto.getUserId())).withRel("departments"))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/departments")
    public CollectionModel<EntityModel<InstituteDepartmentDTO>> departments(@PathVariable("id") Long userId){
        List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(userId);
        List<EntityModel<InstituteDepartmentDTO>> dtos = userDepartments.stream().map(userDepartment -> {
            InstituteDepartmentDTO dto = new InstituteDepartmentDTO();

            dto.setDepartmentName(userDepartment.getInstituteDepartment().getDepartmentName());
            dto.setId(userDepartment.getInstituteDepartment().getId());
            dto.setInstituteId(userDepartment.getInstituteDepartment().getInstitute().getId());
            dto.setPrivileges(userDepartment.getInstituteDepartment()
                    .getPrivileges()
                    .stream()
                    .map(instituteDepartmentPrivilege -> {
                        return privilegeMapper.entityToDTO(instituteDepartmentPrivilege.getPrivilege());
                    })
                    .collect(Collectors.toList()));

            return EntityModel.of(dto); //Add Links To Department
        }).collect(Collectors.toList());

        return CollectionModel.of(dtos);
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
