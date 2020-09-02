package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.EmployeeResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserProfileMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserLoginFailed;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacade {

    private static final Logger log = LoggerFactory.getLogger(UserFacade.class);

    @Autowired
    ParentFacade parentFacade;

    @Autowired
    StudentFacade studentFacade;

    @Autowired
    UserProfileMapper userProfileMapper;

    @Autowired
    UserService userService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserResponseMapper userResponseMapper;

    @Autowired
    EmployeeFacade employeeFacade;

    @Autowired
    TeacherFacade teacherFacade;

    @Autowired
    EmployeeResponseMapper employeeResponseMapper;

    public UserLoginResponseDTO newUser(){
        return null;
    }

    public List<UserLoginResponseDTO> findAll(){
        return userService.findAll()
                .stream()
                .map(user -> userResponseMapper.entityToDTO(user))
                .collect(Collectors.toList());
    }

    public UserLoginResponseDTO findById(Long id){
        return userResponseMapper.entityToDTO(userService.findById(id));
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO requestDTO){
        User user = userService.login(requestDTO.getUsername());
        if (user == null || user.getPassword() == null || !user.getPassword().equals(requestDTO.getPassword())){
            throw new UserLoginFailed(requestDTO.getUsername());
        }

//        if (user.getUserType() == UserType.EMPLOYEE){
//            Employee employee = employeeService.getOne(user.getId())
//                    .orElseThrow(()->new EmployeeNotFoundException(user.getId()));
//            if (employee.getEmployeeType() == EmployeeType.TEACHER)
//                return teacherFacade.findById(user.getId());
//            else
//                return employeeResponseMapper.toDTO(employee);
//        }
//        else if(user.getUserType() == UserType.STUDENT){
//            return studentFacade.getOne(user.getId());
//        }
//        else if(user.getUserType() == UserType.PARENT){
//            return parentFacade.findById(user.getId());
//        }

        UserLoginResponseDTO dto = new UserLoginResponseDTO();
        dto.setUserId(user.getId());
        dto.setApiKey(null);
        dto.setResponseMessage("Login Success");

        return dto;
    }

    public InstituteDTO getInstitute(String userId){

        return null;
    }

}
