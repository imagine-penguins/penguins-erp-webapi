package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.UserNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.LeaveRequestRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<Privilege> getPrivilege(String username, String endPoint){
        return null;
    }

    public List<User> findByDepartmentId(Long departmentId) {
        return userRepository.findByUserDepartmentsInstituteDepartmentId(departmentId);
    }

    public String generateRandomPassword(String email) {
        String temp = email.substring(1, email.indexOf('@'));
        return "" + Character.toUpperCase(email.charAt(0))
                + shuffle(temp.substring(0, (int)(Math.random() * temp.length()))).toLowerCase(Locale.ROOT)
                + randomChar()
                + (int)Math.random() * 1000;
    }

    private Character randomChar() {
        List<Character> characters = Arrays.asList('_', '#', '!', '@');
        return characters.get((int) (Math.random() * characters.size()));
    }

    private String shuffle(String input){
        List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    public String generateUsername(String email, Long userId) {
        return email.substring(0, 5) + "_" + userId;
    }

}
