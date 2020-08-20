package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDepartmentDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartmentPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteDepartmentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InstituteDepartmentFacade {

    @Autowired
    InstituteDepartmentService instituteDepartmentService;

    @Autowired
    InstituteService instituteService;

    @Autowired
    PrivilegeService privilegeService;
    @Autowired
    InstituteDepartmentPrivilegeRepository departmentPrivilegeRepository;

    public InstituteDepartmentDTO createNew(InstituteDepartmentDTO dto){

        InstituteDepartment instituteDepartment = convertToInstituteDepartmentEntity(dto);
        InstituteDepartment newInstituteDepartment = instituteDepartmentService.save(instituteDepartment);

        Set<InstituteDepartmentPrivilege> departmentPrivileges = dto.getPrivileges()
                .stream()
                .map(id -> new InstituteDepartmentPrivilege(newInstituteDepartment, privilegeService.getReference(id)))
                .collect(Collectors.toSet());

        departmentPrivilegeRepository.saveAll(departmentPrivileges);
        return convertToInstituteDepartmentDTO(newInstituteDepartment);
    }

    public InstituteDepartmentDTO findById(Long id){
        return convertToInstituteDepartmentDTO(instituteDepartmentService.findOneByDepartmentId(id));
    }

    private InstituteDepartmentDTO convertToInstituteDepartmentDTO(InstituteDepartment instituteDepartment){
        List<Integer> privileges = instituteDepartment.getPrivileges()
                .stream()
                .map(privilege->privilege.getPrivilege().getId())
                .collect(Collectors.toList());
        InstituteDepartmentDTO dto = new InstituteDepartmentDTO(instituteDepartment.getDepartmentName(), instituteDepartment.getInstitute().getId(), privileges);
        return  dto;
    }

    private InstituteDepartment convertToInstituteDepartmentEntity(InstituteDepartmentDTO dto){
        Institute institute = instituteService.findById(dto.getInstituteId());
        InstituteDepartment instituteDepartment = new InstituteDepartment(dto.getDepartmentName(), institute);
        //InstituteDepartmentPrivilege departmentPrivilege = new InstituteDepartmentPrivilege();
        return  instituteDepartment;
    }

}
