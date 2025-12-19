package com.java25.java25.springboot4.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;
import org.modelmapper.ModelMapper;



@SpringBootApplication
@EnableJpaAuditing
public class Application {

	
	@Bean
	public ModelMapper modelMapper() {
	        return new ModelMapper();
	}	
	
	@Bean
	public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	}	
	
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
	
}
