package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.EmployeeLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.InstituteMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserProfileMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserLoginResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.EmployeeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserLoginFailed;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.ParentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    UserLoginResponseMapper userLoginResponseMapper;

    @Autowired
    EmployeeFacade employeeFacade;

    @Autowired
    TeacherFacade teacherFacade;

    @Autowired
    EmployeeLoginResponseMapper employeeLoginResponseMapper;

    @Autowired
    InstituteMapper instituteMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    ParentService parentService;

    public UserLoginResponseDTO newUser(){
        return null;
    }

    public List<UserLoginResponseDTO> findAll(){
        return userService.findAll()
                .stream()
                .map(user -> userLoginResponseMapper.entityToDTO(user))
                .collect(Collectors.toList());
    }

    public UserLoginResponseDTO findById(Long id){
        return userLoginResponseMapper.entityToDTO(userService.findById(id));
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

    public List<InstituteDTO> getInstitutes(Long userId){
        User user = userService.findById(userId);
        List<InstituteDTO> institutes = new ArrayList<>();
        log.info("User Type-------------->: {}", user.getUserType());
        if (user.getUserType() == UserType.EMPLOYEE){
            Employee employee = employeeService.findById(userId)
                    .orElseThrow(()->new EmployeeNotFoundException(userId));

            institutes.add(instituteMapper.entityToDTO(employee.getInstitute()));
//            if (employee.getEmployeeType() == EmployeeType.TEACHER)
//                return teacherFacade.findById(user.getId());
//            else
//                return employeeResponseMapper.toDTO(employee);
        }
        else if(user.getUserType() == UserType.STUDENT){
            institutes.add(instituteMapper.entityToDTO(studentService
                    .one(userId)
                    .getInstituteClassSection()
                    .getInstituteClass()
                    .getInstitute()));
        }
        else if(user.getUserType() == UserType.PARENT){
//            instituteMapper.entityToDTO(
                    parentService.findById(userId)
                            .getStudents()
                            .stream()
                            .map(student -> instituteMapper.entityToDTO(student
                                    .getInstituteClassSection()
                                    .getInstituteClass()
                                    .getInstitute()))
                            .forEach(institutes::add);

//            parentFacade.findById(user.getId());
        }
        return institutes;
    }

}
