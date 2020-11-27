package com.knackitsolutions.crm.imaginepenguins.dbservice.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

public class AccessJwtToken implements JwtToken {
    private final String rawToken;
    @JsonIgnore
    private Claims claims;

    protected AccessJwtToken(final String rawToken, final Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return this.claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }
}
