package com.knackitsolutions.crm.imaginepenguins.dbservice.security.endpoint;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.extractor.TokenExtractor;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.verifier.TokenVerifier;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.config.JwtSettings;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.config.WebSecurityConfig;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.exceptions.InvalidJwtToken;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.UserContext;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.token.JwtToken;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.token.JwtTokenFactory;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.token.RawAccessJwtToken;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.token.RefreshToken;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RefreshTokenEndpoint {
    @Autowired private JwtTokenFactory tokenFactory;
    @Autowired private JwtSettings jwtSettings;
    @Autowired private UserService userService;
    @Autowired private TokenVerifier tokenVerifier;
    @Autowired
    @Qualifier("jwtHeaderTokenExtractor") private TokenExtractor tokenExtractor;

    @RequestMapping(value="/auth/token", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String subject = refreshToken.getSubject();
        User user = userService.getByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        if (user.getUserPrivileges() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
        List<GrantedAuthority> authorities = user.getUserPrivileges().stream()
                .map(authority -> new SimpleGrantedAuthority(
                                authority
                                        .getDepartmentPrivilege()
                                        .getPrivilege()
                                        .getPrivilegeCode()
                                        .getPrivilegeCode()
                        )
                )
                .collect(Collectors.toList());
        UserContext userContext = UserContext.create(user.getId(), user.getUsername(), authorities);
        return tokenFactory.createAccessJwtToken(userContext);
    }
}
