package com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.extractor;

public interface TokenExtractor {
    public String extract(String payload);
}
