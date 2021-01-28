package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.DashboardController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.DepartmentController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.UserControllerImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.InstituteMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacade {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final UserLoginResponseMapper userLoginResponseMapper;
    private final InstituteMapper instituteMapper;
    private final StudentServiceImpl studentServiceImpl;
    private final ParentService parentService;
    private final UserDepartmentRepository userDepartmentRepository;
    private final StudentService studentService;

    public List<UserLoginResponseDTO> findAll(){
        return userService.findAll()
                .stream()
                .map(user -> userLoginResponseMapper.entityToDTO(user))
                .collect(Collectors.toList());
    }

    public UserLoginResponseDTO findById(Long id){
        return userLoginResponseMapper.entityToDTO(userService.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<InstituteDTO> getInstitutes(Long userId){
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<InstituteDTO> institutes = new ArrayList<>();
        log.info("UserDTO Type-------------->: {}", user.getUserType());
        if (user.getUserType() == UserType.EMPLOYEE){
            Employee employee = employeeService.findById(userId)
                    .orElseThrow(()->new EmployeeNotFoundException(userId));

            institutes.add(instituteMapper.entityToDTO(employee.getInstitute()));
        }
        else if(user.getUserType() == UserType.STUDENT){
            institutes.add(instituteMapper.entityToDTO(studentServiceImpl
                    .one(userId)
                    .getInstituteClassSection()
                    .getInstituteClass()
                    .getInstitute()));
        }
        else if(user.getUserType() == UserType.PARENT){
                    parentService.findById(userId)
                            .getStudents()
                            .stream()
                            .map(student -> instituteMapper.entityToDTO(student
                                    .getInstituteClassSection()
                                    .getInstituteClass()
                                    .getInstitute()))
                            .forEach(institutes::add);
        }
        return institutes;
    }

    public List<Link> loginLinks(Long userId){
        List<UserDepartment> userDepartments = userDepartmentRepository.findByUserId(userId);
        Long departmentId = userDepartments.get(0).getInstituteDepartment().getId();
        return Stream.of(linkTo(methodOn(UserControllerImpl.class).one(userId)).withSelfRel(),
                linkTo(methodOn(UserControllerImpl.class).all(0, 10, null, null)).withRel("users"),
                linkTo(methodOn(DashboardController.class).webDashboardDTO())
                        .withRel("web-dashboard"),
                linkTo(methodOn(DashboardController.class)
                        .appDashboardDTO()).withRel("app-dashboard"),
                linkTo(methodOn(UserControllerImpl.class).myInstitute()).withRel("institute")
                ,                linkTo(methodOn(DepartmentController.class).myDepartments()).withRel("departments")
        ).collect(Collectors.toList());
    }

    public UserListDTO newUserListDTO(List<UserListDTO.UserDTO> users, int page, int size, int totalUsers) {
        int totalPages = (int) Math.ceil(totalUsers / size);
        UserListDTO userListDTO = new UserListDTO();

        userListDTO.setUserDTOS(users);
        userListDTO.setPageNumber(page);
        userListDTO.setPageSize(size);
        userListDTO.setTotalUsers(totalUsers);
        userListDTO.setTotalPages(totalPages);
        return userListDTO;
    }
}
