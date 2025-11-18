package com.emts.smart_attendance_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * *******************************************************************
 * File: SwaggerConfig.java
 * Package: com.emts.smart_attendance_system.config
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 30/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8083");
        localServer.setDescription("Local development server");

        Server productionServer = new Server();
        productionServer.setUrl("https://smart-attendance-system-production-253e.up.railway.app");
        productionServer.setDescription("Production server on Railway");

        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(localServer, productionServer))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, createSecurityScheme())
                );
    }

    private Info apiInfo() {
        return new Info()
                .title("eMTS Smart Attendance System API")
                .description("REST API for Smart Attendance System with JWT Authentication")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Mohamed Taha Elmeligy")
                        .email("mt.elmeligy.community@gmail.com")
                        .url("https://www.linkedin.com/in/mtelmeligy-backend-dev/")
                )
                .license(new License()
                        .name("© 2025 eMTS (e Modern Tech Solutions)")
                        .url("mt.elmeligy.community@gmail.com")
                );
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT token (without 'Bearer ' prefix)");
    }
}
