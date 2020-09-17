package com.knackitsolutions.crm.imaginepenguins.dbservice.security.config;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.request.RequestParameterConverter;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @Value("${angular-client:http://localhost:4200}")
    private String angularClient;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(angularClient)
                .allowedMethods("GET", "PUT", "DELETE", "POST");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Map<String, Object> components = listableBeanFactory.getBeansWithAnnotation(RequestParameterConverter.class);
        components.values().parallelStream().forEach(
                c -> {
                    if (c instanceof Converter) {
                        registry.addConverter((Converter) c);
                    }
                }
        );
    }

}
