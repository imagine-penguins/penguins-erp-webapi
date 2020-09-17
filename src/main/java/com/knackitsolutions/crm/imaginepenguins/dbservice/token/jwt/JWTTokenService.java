package com.knackitsolutions.crm.imaginepenguins.dbservice.token.jwt;

import com.knackitsolutions.crm.imaginepenguins.dbservice.token.api.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import static io.jsonwebtoken.impl.TextCodec.BASE64;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@Service
public final class JWTTokenService implements Clock, TokenService {

    private final static String DOT = ".";
    private final static GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private String issuer;

    private String secretKey;

    private Integer expirationSec;

    private Integer clockSkewSec;

    JWTTokenService(@Value("${jwt.issuer:imaginepenguins}") @NonNull String issuer
            , @Value("${jwt.secret:imaginepenguins-erp}") @NonNull String secretKey
            , @Value("${jwt.expiration-sec:900}") @NonNull Integer expirationSec
            , @Value("${jwt.clock-skew-sec:300}") @NonNull Integer clockSkewSec) {
        super();
        this.issuer = issuer;
        this.secretKey = BASE64.encode(secretKey);
        this.expirationSec = expirationSec;
        this.clockSkewSec = clockSkewSec;

    }

    @Override
    public String permanent(final Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String expiring(final Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    private String newToken(final Map<String, String> attributes, final Integer expirationSec) {
        final Claims claims = Jwts
                .claims()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer(issuer);

        if (expirationSec > 0) {
            final Date expirationDate = new Date(System.currentTimeMillis() + (expirationSec * 1000));
            claims.setExpiration(expirationDate);
        }

        claims.putAll(attributes);
        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    @Override
    public Map<String, String> untrusted(String token) {
        final JwtParser jwtParser = getJwtUnsignedParser();

        final String withoutSignature = subStringBeforeLast(token, DOT) + DOT;
        return parseClaims(() -> jwtParser.parseClaimsJws(withoutSignature).getBody());
    }

    private String subStringBeforeLast(String value, String part) {
        int lastIndex = value.lastIndexOf(part);
        return value.substring(0, lastIndex);
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> claimsSupplier) {
        try {
            final Claims claims = claimsSupplier.get();
            final Map<String, String> parsedClaims = new HashMap<>();
            for (final Map.Entry<String, Object> e : claims.entrySet()) {
                parsedClaims.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return Collections.unmodifiableMap(parsedClaims);
        } catch (final IllegalArgumentException | JwtException e) {
            return new HashMap<>();
        }
    }

    private JwtParser getJwtSingedParser(){return getJwtParser().setSigningKey(secretKey);}

    private JwtParser getJwtUnsignedParser() {
        return getJwtParser();
    }
    private JwtParser getJwtParser() {
        JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec);
        return parser;
    }

    @Override
    public Map<String, String> verify(String token) {
        final JwtParser jwtParser = getJwtSingedParser();
        return parseClaims(()-> jwtParser.parseClaimsJws(token).getBody());
    }

    @Override
    public Date now() {
        final Date now = new Date();
        return now;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getExpirationSec() {
        return expirationSec;
    }

    public void setExpirationSec(Integer expirationSec) {
        this.expirationSec = expirationSec;
    }

    public Integer getClockSkewSec() {
        return clockSkewSec;
    }

    public void setClockSkewSec(Integer clockSkewSec) {
        this.clockSkewSec = clockSkewSec;
    }
}
