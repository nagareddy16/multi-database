package com.naga.multi_database;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
		info = @Info(
				title = "Service for the mysql sql & mongodb",
				description = "MultiDb service for REST API Documentation",
				version = "v1"
		)
)
public class MultiDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiDatabaseApplication.class, args);
	}

}
