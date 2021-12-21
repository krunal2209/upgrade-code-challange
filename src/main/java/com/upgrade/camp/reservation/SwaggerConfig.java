package com.upgrade.camp.reservation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(
						new Info().title("Camp Reservation API")
								.description("APIs related to reserving camp site at underwater volcano in the Pacific Ocean.")
								.version("v0.0.1")
				);
	}
}
