package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDashboardDTO {
    private List<PrivilegeDTO> privileges ;
}
