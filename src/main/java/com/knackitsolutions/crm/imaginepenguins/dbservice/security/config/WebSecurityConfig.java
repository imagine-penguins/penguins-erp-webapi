package com.knackitsolutions.crm.imaginepenguins.dbservice.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.RestAuthenticationEntryPoint;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.form.FormAuthenticationProvider;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.form.FormAwareAuthenticationFailureHandler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.form.FormAwareAuthenticationSuccessHandler;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.form.FormLoginProcessingFilter;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.JwtAuthenticationProvider;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.SkipPathRequestMatcher;
import com.knackitsolutions.crm.imaginepenguins.dbservice.security.auth.jwt.extractor.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/auth/login";
    public static final String REFRESH_TOKEN_URL = "/auth/token";
    public static final String API_ROOT_URL = "/**";

    @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private FormAwareAuthenticationSuccessHandler successHandler;
    @Autowired private FormAwareAuthenticationFailureHandler failureHandler;
    @Autowired private FormAuthenticationProvider formAuthenticationProvider;
    @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired private TokenExtractor tokenExtractor;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ObjectMapper objectMapper;

    protected FormLoginProcessingFilter buildFormLoginProcessingFilter(String loginEntryPoint) throws Exception {
        FormLoginProcessingFilter filter = new FormLoginProcessingFilter(loginEntryPoint, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
                = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(formAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(
                AUTHENTICATION_URL,
                REFRESH_TOKEN_URL,
                "/console"
        );

        http
                .cors()
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                    .permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers(API_ROOT_URL)
                    .authenticated() // Protected API End-points
                .and()
                    .addFilterBefore(buildFormLoginProcessingFilter(AUTHENTICATION_URL)
                            , UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList,
                            API_ROOT_URL), UsernamePasswordAuthenticationFilter.class)
                    .csrf().disable()
                    .httpBasic().disable()
                    .formLogin().disable()
                    .logout().disable();
    }

}
