package com.knackitsolutions.crm.imaginepenguins.dbservice.auth.api;


import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserLoginResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Privilege;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.User;
import org.checkerframework.checker.nullness.Opt;

import java.util.List;
import java.util.Optional;

public interface UserAuthenticationService {

    Optional<UserLoginResponseDTO> login(String username, String password);

    Optional<User> findByToken(String token);

    void logout(User user);

    Optional<Privilege> getPrivilege(String username, String endPoint);

}
