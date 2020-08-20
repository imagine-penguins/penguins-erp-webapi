package com.knackitsolutions.crm.imaginepenguins.dbservice;

import static org.assertj.core.api.Assertions.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.EmployeeCreationDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Employee;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootTest
class DbServiceApplicationTests {

//	@TestConfiguration
//	static class EmoloyeeTestConfiguration{
//
//		@Bean
//		public ModelMapper modelMapper(){
//			return new ModelMapper();
//		}
//	}

	@Test
	void contextLoads() {
	}

//	@Autowired
//	ModelMapper modelMapper;

//	@Test
//	void employeeModelMapperTest(){
//		EmployeeCreationDTO dto = new EmployeeCreationDTO(1l, "kill", "kill", "fname", true, "987", "mj11");
//		Employee employee = modelMapper.map(dto, Employee.class);
////		assertThat(dto.getManagerId()).isEqualTo();
//
//	}

}
