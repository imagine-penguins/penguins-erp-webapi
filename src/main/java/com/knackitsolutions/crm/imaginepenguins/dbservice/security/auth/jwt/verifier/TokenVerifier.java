package com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.verifier;

public interface TokenVerifier {
    public boolean verify(String jti);
}
