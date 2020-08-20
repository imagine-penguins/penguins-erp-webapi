package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.InstituteNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituteService {

    @Autowired
    InstituteRepository instituteRepository;

    public Institute newInstitute(Institute institute){return instituteRepository.save(institute);}

    public Institute findById(Integer id){
        return instituteRepository.findById(id)
                .orElseThrow(() -> new InstituteNotFoundException(id));
    }

    public List<Institute> all(){return instituteRepository.findAll();}

    //udpate entity
}
