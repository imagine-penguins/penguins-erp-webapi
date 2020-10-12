package com.knackitsolutions.crm.imaginepenguins.dbservice.facade;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
