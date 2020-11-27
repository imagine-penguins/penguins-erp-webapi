package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.InstituteNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstituteService {

    @Autowired
    InstituteRepository instituteRepository;

    public Institute newInstitute(Institute institute){return instituteRepository.save(institute);}

    public Optional<Institute> findById(Integer id){
        return instituteRepository.findById(id);
    }

    public List<Institute> all(){return instituteRepository.findAll();}

    public Integer findAllUser(Integer instituteId, UserType type){
        Integer total = 0;
//        List<UserDTO> users = ;
        return total;
    }

//    public Integer findAllStudents(Integer instituteId) {
//        return instituteId;
//    }
//
//    public Integer findAllTeachers(Integer instituteId) {
//    }
//
//    public Integer findAllEmployees(Integer instituteId) {
//    }
//
//    public Integer findAllParents(Integer instituteId) {
//    }

    //udpate entity
}
