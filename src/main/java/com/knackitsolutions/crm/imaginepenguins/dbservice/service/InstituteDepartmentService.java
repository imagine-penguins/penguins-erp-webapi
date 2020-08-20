package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.DepartmentNotFoundExeption;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituteDepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    public InstituteDepartment findOneByDepartmentId(Long id){
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundExeption(id));
    }

    public List<InstituteDepartment> findAllByInstitute(Institute institute){
        return departmentRepository.findByInstitute(institute);
    }

    public InstituteDepartment save(InstituteDepartment department){return departmentRepository.save(department);}

    public List<InstituteDepartment> saveAll(List<InstituteDepartment> departments){
        return departmentRepository.saveAll(departments);
    }
}
