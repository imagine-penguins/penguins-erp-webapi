package com.knackitsolutions.crm.imaginepenguins.dbservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(){
//		return args -> {
//			System.out.println("CommandLineRunner running in the DBServiceApplication class...");
//		};
//	}

}
