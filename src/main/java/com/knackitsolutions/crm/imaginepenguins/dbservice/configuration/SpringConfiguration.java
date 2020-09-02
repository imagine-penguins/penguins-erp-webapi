package com.knackitsolutions.crm.imaginepenguins.dbservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfiguration implements WebMvcConfigurer {

    @Value("${angular-client:http://localhost:4200}")
    private String angularClient;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(angularClient)
                .allowedMethods("GET", "PUT", "DELETE", "POST");
    }
}
