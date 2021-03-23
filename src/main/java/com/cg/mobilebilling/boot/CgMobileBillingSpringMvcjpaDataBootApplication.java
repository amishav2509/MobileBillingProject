package com.cg.mobilebilling.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages= {"com.cg.mobilebilling"})
@EntityScan(basePackages="com.cg.mobilebilling.beans")
@EnableJpaRepositories(basePackages="com.cg.mobilebilling.daoservices")
@EnableWebMvc

public class CgMobileBillingSpringMvcjpaDataBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(CgMobileBillingSpringMvcjpaDataBootApplication.class, args);
	}
}
