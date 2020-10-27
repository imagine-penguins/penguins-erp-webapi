package com.knackitsolutions.crm.imaginepenguins.dbservice.security.config;

import com.knackitsolutions.crm.imaginepenguins.dbservice.auth.api.UserAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    private UserAuthenticationService authenticationService;

    TokenAuthenticationProvider(@NonNull UserAuthenticationService authenticationService) {
        super();
        this.authenticationService = authenticationService;
    }



    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        //nothing to do here
    }

    @Override
    protected UserDetails retrieveUser(final String username
            , final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        final Object token = authentication.getCredentials();

        log.debug("token: {}",
                Optional
                        .ofNullable(token)
                        .map(String::valueOf)
                        .orElse(null)
        );
        UserDetails userDetails = Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(authenticationService::findByToken)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token: " + token));

        return userDetails;

    }

    private void validatePrivileges(String username, String endPoint) throws AuthenticationCredentialsNotFoundException{
        authenticationService
                .getPrivilege(username, endPoint)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("UserDTO not authorized: " + username));
    }

    @NonNull
    public UserAuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void setAuthenticationService(@NonNull UserAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

}
