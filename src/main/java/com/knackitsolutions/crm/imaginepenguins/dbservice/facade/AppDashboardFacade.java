package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.AttendanceController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.StudentController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.controller.TeacherController;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AppDashboardDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartmentPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.UserPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserDepartmentRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppDashboardFacade {

    @Autowired
    private PrivilegeMapper privilegeMapper;

    //Get the department from UserDepartment. Find all the privileges in instituteDepartment for that department. Convert that to PrivilegeDTO. Make a list

    @Autowired
    private UserPrivilegeRepository userPrivilegeRepository;

    private PrivilegeDTO addLinks(Long userId, UserPrivilege userPrivilege) {
        Privilege privilege = userPrivilege.getDepartmentPrivilege().getPrivilege();
        PrivilegeDTO dto = privilegeMapper
                .entityToDTO(privilege);

        if (privilege.getPrivilegeCode() == PrivilegeCode.MARK_STUDENT_ATTENDANCE){
            dto.add(linkTo(methodOn(TeacherController.class).classes(userId)).withRel("classes"));
        }
        if (privilege.getPrivilegeCode() == PrivilegeCode.VIEW_SELF_ATTENDANCE);
            //;
        if (privilege.getPrivilegeCode() == PrivilegeCode.VIEW_STUDENTS_ATTENDANCE_HISTORY) {
            dto.add(linkTo(methodOn(AttendanceController.class).attendanceHistory(null, null, null, null)).withRel("view-attendance-history"));
        }
        return dto;
    }

    public List<PrivilegeDTO> getPrivileges(Long userId, Long departmentId){

        return userPrivilegeRepository
                .findByUserIdAndDepartmentId(userId, departmentId)
                .stream()
                .map(userPrivilege -> addLinks(userId, userPrivilege))
                .collect(Collectors.toList());
    }

}
