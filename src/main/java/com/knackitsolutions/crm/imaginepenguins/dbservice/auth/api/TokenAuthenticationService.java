package com.knackitsolutions.crm.imaginepenguins.dbservice.auth.api;

import com.google.common.collect.ImmutableMap;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.token.api.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TokenAuthenticationService implements UserAuthenticationService {

    @NonNull
    TokenService tokenService;

    @NonNull
    UserService userService;

    @NonNull
    PasswordEncoder passwordEncoder;

    TokenAuthenticationService(TokenService tokenService, UserService userService, PasswordEncoder passwordEncoder) {
        super();
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserLoginResponseDTO> login(String username, String password) {
        log.debug("username: {}", username);
        return Optional.ofNullable(userService
                .login(username))
                .filter(user -> {
                    log.debug("User found now matching the password");
                    return passwordEncoder.matches(password, user.getPassword());
                })
                .map(user -> {
                    log.debug("password in request: {}, in db: {}", password, user.getPassword());
                    UserLoginResponseDTO dto = new UserLoginResponseDTO();
                    dto.setUserId(user.getId());
                    dto.setResponseMessage("Login Success");
                    dto.setApiKey(tokenService.expiring(ImmutableMap.of("username", username)));
                    return dto;
                });
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .map(username -> userService.login(username));
    }

    @Override
    public Optional<Privilege> getPrivilege(String username, String endPoint){
        return userService
                .getPrivilege(username, endPoint);
    }

    @Override
    public void logout(User user) {
        //Nothing to do
    }

}
