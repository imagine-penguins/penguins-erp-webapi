package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User login(String username){
        User user = userRepository.findByUsername(username);
        return user;
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<Privilege> getPrivilege(String username, String endPoint){
        return null;
    }

    public LeaveRequest saveLeaveRequest(Optional<LeaveRequest> leaveRequest) {
        return leaveRequest.map(leaveRequestRepository::save).orElse(null);
    }

    public List<User> findByDepartmentId(Long departmentId) {
        return userRepository.findByUserDepartmentsInstituteDepartmentId(departmentId);
    }

}
