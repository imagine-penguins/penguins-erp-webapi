package com.knackitsolutions.crm.imaginepenguins.dbservice.token.api;

import java.util.Map;
import java.util.Optional;

public interface TokenService {

    String permanent(Map<String, String> attributes);

    String expiring(Map<String, String> attributes);

    Map<String, String> untrusted(String token);

    Map<String, String> verify(String token);

}
