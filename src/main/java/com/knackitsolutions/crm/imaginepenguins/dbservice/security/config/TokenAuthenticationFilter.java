package com.knackitsolutions.crm.imaginepenguins.dbservice.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private static final String BEARER = "Bearer";

    TokenAuthenticationFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request
            , final HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String endPoint = request.getRequestURL().toString();

        log.debug("Request URL: {}", endPoint);
        final String param = Optional.ofNullable(request.getHeader("Authorization"))
                .orElse(request.getParameter("token"));

        final String token = Optional.ofNullable(param)
                .map(value -> removeFromStartAndTrim(value, BEARER))
                .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token."));

        final Authentication authRequest = new UsernamePasswordAuthenticationToken(endPoint, token);

        return getAuthenticationManager().authenticate(authRequest);
    }


    private final String removeFromStartAndTrim(String value, String remove) {
        return value.replaceFirst(remove, "").trim();
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request
            , final HttpServletResponse response
            , final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
