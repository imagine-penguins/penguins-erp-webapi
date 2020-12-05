package com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.JwtAuthenticationToken;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.config.JwtSettings;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;
    public JwtAuthenticationProvider(final JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        Long id = Long.parseLong(jwsClaims.getBody().getId());
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        UserType userType = UserType.of(jwsClaims.getBody().get("usertype", String.class));
        log.debug("userType: " + userType);
        Integer instituteId = jwsClaims.getBody().get("institute", Integer.class);
        Long instituteClassSectionId = jwsClaims.getBody().get("instituteClassSection", Long.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserContext context = UserContext.create(id, subject, userType, instituteId, instituteClassSectionId, authorities);

        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
