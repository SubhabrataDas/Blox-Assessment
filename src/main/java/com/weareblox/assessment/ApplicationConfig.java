package com.weareblox.assessment;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "BLOX Assessment API",
        version = "1.0",
        description = "API for demonstrating problem solving skills"))
public class ApplicationConfig {
	
}
