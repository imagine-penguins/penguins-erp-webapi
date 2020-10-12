package com.knackitsolutions.crm.imaginepenguins.dbservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.LeaveRequestStatus;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.InstituteMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.LeaveRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.IAuthenticationFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.UserFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/users")
public class UserControllerImpl {

    @Autowired
    private IAuthenticationFacade authenticationFacade;
    private static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);

    @Autowired
    UserFacade userFacade;

    @Autowired
    UserService userService;

    @Autowired
    UserDepartmentRepository userDepartmentRepository;

    @Autowired
    PrivilegeMapper privilegeMapper;

    @Autowired
    LeaveRequestMapper leaveRequestMapper;

    @Autowired
    EmployeeController employeeController;

    @Autowired
    StudentController studentController;

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    @Autowired
    InstituteDepartmentRepository instituteDepartmentRepository;

    @Autowired
    InstituteMapper instituteMapper;

    @GetMapping
    public CollectionModel<EntityModel<UserLoginResponseDTO>> all() {
        List<EntityModel<UserLoginResponseDTO>> userList = userFacade.findAll()
                .stream()
                .map(user -> {
                    try {
                        return EntityModel.of(user, loginLinks(user));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        return CollectionModel.of(userList, linkTo(methodOn(UserControllerImpl.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserLoginResponseDTO> one(@PathVariable("id") Long id){
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
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO requestDTO) {
        UserLoginResponseDTO dto = userFacade.authLogin(requestDTO);
        return ResponseEntity.ok(EntityModel.of(dto, loginLinks(dto)));
    }

    private List<Link> loginLinks(UserLoginResponseDTO dto){
        List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(dto.getUserId());
        Long departmentId = userDepartments.get(0).getInstituteDepartment().getId();
        return Stream.of(linkTo(methodOn(UserControllerImpl.class).one(dto.getUserId())).withSelfRel(),
                linkTo(methodOn(UserControllerImpl.class).all()).withRel("users"),
                linkTo(methodOn(DashboardController.class).webDashboardDTO(departmentId))
                        .withRel("web-dashboard"),
                linkTo(methodOn(DashboardController.class)
                        .appDashboardDTO(departmentId)).withRel("app-dashboard"),
                linkTo(methodOn(UserControllerImpl.class).institute(dto.getUserId())).withRel("institute")
                ,                linkTo(methodOn(UserControllerImpl.class).myDepartments()).withRel("departments")
                ).collect(Collectors.toList());
    }

    @GetMapping("/departments")
    public CollectionModel<EntityModel<InstituteDepartmentDTO>> myDepartments(){
        User user = (User) authenticationFacade.getAuthentication().getPrincipal();

        List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(user.getId());
        List<EntityModel<InstituteDepartmentDTO>> dtos = userDepartments.stream().map(userDepartment -> {
            InstituteDepartmentDTO dto = new InstituteDepartmentDTO();

            dto.setDepartmentName(userDepartment.getInstituteDepartment().getDepartmentName());
            dto.setId(userDepartment.getInstituteDepartment().getId());
            dto.setInstituteId(userDepartment.getInstituteDepartment().getInstitute().getId());
            dto.setPrivileges(userDepartment.getInstituteDepartment()
                    .getPrivileges()
                    .stream()
                    .map(instituteDepartmentPrivilege -> {
                        try {
                            return privilegeMapper.entityToDTO(instituteDepartmentPrivilege.getPrivilege());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .collect(Collectors.toList()));

            return EntityModel.of(dto); //Add Links To Department
        }).collect(Collectors.toList());

        return CollectionModel.of(dtos);
    }

    @GetMapping("/{id}/institute")
    public CollectionModel<EntityModel<InstituteDTO>> institute(@PathVariable("id") Long id) {
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
            return EntityModel.of(instituteDTO);
        }).collect(Collectors.toList());

        return CollectionModel.of(institutes,
                linkTo(methodOn(InstituteControllerImpl.class).all()).withRel("institutes"));

    }

    @GetMapping("/attendance")
    public CollectionModel<?> viewAttendance(@RequestParam(name = "period") Optional<AttendanceController.Period> period
            , @RequestParam(name = "value") Optional<String> value) {
        User user = (User) authenticationFacade.getAuthentication().getPrincipal();
        log.debug("Student attendance history for period: {}, value: {}, studentId: {}", period, value, user.getId());
        List<UserAttendanceResponseDTO> dtos = null;
        if (user.getUserType() == UserType.EMPLOYEE) {
            return CollectionModel.of(employeeController.viewAttendance(user.getId(), period, value));
        } else if (user.getUserType() == UserType.STUDENT) {
            return CollectionModel.of(studentController.viewAttendance(user.getId(), period, value));
        } else
            return CollectionModel.of(null);
    }

    @PostMapping("/attendance/leave-request")
    public ResponseEntity<String> saveLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequest entity = leaveRequestMapper.dtoToEntity(leaveRequestDTO);
        entity.setLeaveRequestStatus(LeaveRequestStatus.PENDING);
        LeaveRequest leaveRequest = userService
                .saveLeaveRequest(Optional.ofNullable(entity));

        return ResponseEntity.status(HttpStatus.CREATED).body("Leave Request saved.");
    }

    @GetMapping("/attendance/leave-request")
    public LeaveHistoryDTO leaveRequestHistory() {
        LeaveHistoryDTO dto = new LeaveHistoryDTO();
        User user = (User) authenticationFacade.getAuthentication().getPrincipal();
        List<LeaveResponseDTO> response = leaveRequestRepository.findByUserId(user.getId())
                .stream()
                .map(leaveRequestMapper::entityToDTO)
                .collect(Collectors.toList());
        dto.setLeaveResponseDTO(response);
        List<LeaveHistoryDTO.GraphData> graphData = leaveRequestMapper
                .getMonthlyLeaveCount(leaveRequestMapper.getUserLeavesDates(user))
                .entrySet().stream().map(leaveRequestMapper::getGraphDataFromMapEntry).collect(Collectors.toList());

        dto.setGraphData(graphData);
        return dto;
    }

    @PutMapping("/attendance/leave-request/{leaveRequestId}")
    public ResponseEntity<String> updateLeaveRequest(@PathVariable("leaveRequestId") Long leaveRequestId
            , @RequestBody LeaveRequestUpdateDTO dto) {
        LeaveRequest oldLeaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave Request Not Found With the provided Id"));
        leaveRequestMapper.dtoToEntity(oldLeaveRequest, dto);
        LeaveRequest newLeaveRequest = leaveRequestRepository.save(oldLeaveRequest);
        if (newLeaveRequest != null)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Leave Updated.");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Leave not Updated.");
    }

    @GetMapping("/institute/departments")
    public CollectionModel<EntityModel<InstituteDepartmentDTO>> loadDepartments(){
        User user = (User) authenticationFacade.getAuthentication();
        Employee employee = null;
        if (!(user instanceof Employee)) {
            throw new RuntimeException("User is not an Employee of any institute.");
        }
        employee = (Employee) user;
        List<EntityModel<InstituteDepartmentDTO>> dtos = instituteMapper.entityToDTO(instituteDepartmentRepository
                .findByInstituteId(employee.getInstitute().getId()))
                .stream()
                .map(EntityModel::of)
                .map(em -> em.add(
                        linkTo(methodOn(EmployeeController.class)
                                .loadEmployeesByDepartment(em.getContent().getId())).withRel("load-employees")
                ))
                .collect(Collectors.toList());

        return CollectionModel.of(dtos
                , linkTo(methodOn(UserControllerImpl.class).loadDepartments()).withSelfRel()
                , linkTo(methodOn(TeacherController.class).classes()).withRel("load-classes"));
    }

}
