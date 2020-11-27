package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO extends RepresentationModel<UserLoginResponseDTO> {
    private String token;
    private String refreshToken;
    private String responseMessage;
}
