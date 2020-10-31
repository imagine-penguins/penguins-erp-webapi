package com.knackitsolutions.crm.imaginepenguins.dbservice.security.config;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.converter.RequestParameterConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @Value("${angular-client:http://localhost:4200}")
    private String[] angularClient;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.debug("Cors Origins are listed below:");
        Arrays.stream(angularClient).forEach(client -> log.debug("Cors Origin: {}", client));
        registry.addMapping("/**")
                .allowedOrigins(angularClient)
                .allowedMethods("GET", "PUT", "DELETE", "POST");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Map<String, Object> components = listableBeanFactory.getBeansWithAnnotation(RequestParameterConverter.class);
        components
                .values()
                .parallelStream()
                .filter(c -> c instanceof Converter)
                .map(c -> (Converter) c)
                .forEach(registry::addConverter);
    }

}
