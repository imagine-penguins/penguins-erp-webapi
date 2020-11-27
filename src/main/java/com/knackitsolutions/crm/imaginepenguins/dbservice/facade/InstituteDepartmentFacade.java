package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.InstituteDepartmentDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartment;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.InstituteDepartmentPrivilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.InstituteNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteDepartmentPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteDepartmentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.InstituteService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
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

    @Autowired
    PrivilegeMapper privilegeMapper;

    public InstituteDepartmentDTO createNew(InstituteDepartmentDTO dto){

        InstituteDepartment instituteDepartment = convertToInstituteDepartmentEntity(dto);
        InstituteDepartment newInstituteDepartment = instituteDepartmentService.save(instituteDepartment);

        Set<InstituteDepartmentPrivilege> departmentPrivileges = dto.getPrivileges()
                .stream()
                .map(privilegeDTO -> new InstituteDepartmentPrivilege(newInstituteDepartment, privilegeService.getReference(privilegeDTO.getId())))
                .collect(Collectors.toSet());

        departmentPrivilegeRepository.saveAll(departmentPrivileges);
        return convertToInstituteDepartmentDTO(newInstituteDepartment);
    }

    public InstituteDepartmentDTO findById(Long id){
        return convertToInstituteDepartmentDTO(instituteDepartmentService.findOneByDepartmentId(id));
    }

    public InstituteDepartmentDTO convertToInstituteDepartmentDTO(InstituteDepartment instituteDepartment){
        List<PrivilegeDTO> privileges = instituteDepartment.getPrivileges()
                .stream()
                .map(privilege-> {
                    try {
                        return privilegeMapper.entityToDTO(privilege.getPrivilege());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        InstituteDepartmentDTO dto = new InstituteDepartmentDTO();
        dto.setPrivileges(privileges);
        dto.setDepartmentName(instituteDepartment.getDepartmentName());
        dto.setInstituteId(instituteDepartment.getInstitute().getId());
        dto.setId(instituteDepartment.getId());
        return  dto;
    }

    public InstituteDepartment convertToInstituteDepartmentEntity(InstituteDepartmentDTO dto){
        Institute institute = instituteService.findById(dto.getInstituteId()).orElseThrow(() -> new InstituteNotFoundException(dto.getInstituteId()));
        InstituteDepartment instituteDepartment = new InstituteDepartment(dto.getDepartmentName(), institute);
        return  instituteDepartment;
    }

}
