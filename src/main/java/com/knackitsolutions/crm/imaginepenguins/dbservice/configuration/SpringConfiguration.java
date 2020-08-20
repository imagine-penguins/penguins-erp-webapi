package com.knackitsolutions.crm.imaginepenguins.dbservice.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
//        mapper.addMappings();
        return new ModelMapper();
    }

    

}
