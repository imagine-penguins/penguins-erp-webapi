package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstituteDepartmentDTO {

    private Long id;

    private String departmentName;

    private Integer instituteId;

    private List<PrivilegeDTO> privileges = new ArrayList<>();

    private Boolean primary;

}
