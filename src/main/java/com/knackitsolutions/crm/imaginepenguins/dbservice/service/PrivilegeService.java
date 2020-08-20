package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.PrivilegeNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService {
    @Autowired
    PrivilegeRepository privilegeRepository;

    public Privilege get(Integer id){
        return privilegeRepository.findById(id)
            .orElseThrow(() -> new PrivilegeNotFoundException(id));
    }

    public Privilege getReference(Integer id){return privilegeRepository.getOne(id);}

    public Privilege save(Privilege privilege){
        if (privilege != null)
            return privilegeRepository.save(privilege);
        return null;
    }

    public List<Privilege> all(){return privilegeRepository.findAll();}

    public Privilege updatePrivilege(Integer id, Privilege privilege){
        Privilege oldPPrivilege = privilegeRepository.findById(id)
                .orElseThrow(() -> new PrivilegeNotFoundException(id));
        oldPPrivilege.setPrivilegeName(privilege.getPrivilegeName());
        oldPPrivilege.setPrivilegeDesc(privilege.getPrivilegeDesc());
        return privilegeRepository.save(privilege);
    }
}
