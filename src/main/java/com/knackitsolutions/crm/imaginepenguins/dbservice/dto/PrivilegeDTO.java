package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDTO extends RepresentationModel<PrivilegeDTO> {
    private Integer id;

    private String name;

    private String bgImg;
}
