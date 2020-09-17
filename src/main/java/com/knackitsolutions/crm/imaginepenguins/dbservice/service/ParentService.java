package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Parent;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.ParentNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentService {
    @Autowired
    ParentRepository parentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Parent findById(Long id){
        return parentRepository.findById(id)
                .orElseThrow(()->new ParentNotFoundException(id));
    }

    public List<Parent> parents(){
        return parentRepository.findAll();
    }

    public Parent newParent(Parent parent) {
        parent.setPassword(passwordEncoder.encode(parent.getPassword()));
        return parentRepository.save(parent);
    }
}
