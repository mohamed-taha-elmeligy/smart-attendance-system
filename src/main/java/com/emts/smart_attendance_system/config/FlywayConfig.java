package com.emts.smart_attendance_system.config;

import org.springframework.context.annotation.Configuration;

/**
 * *******************************************************************
 * File: FlywayConfig.java
 * Package: com.emts.smart_attendance_system.config
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 26/10/2025
 * Port Number: 8083
 * *******************************************************************

 * Flyway Configuration

 * Spring Boot Auto Configuration
 *   application.properties:
 *   spring.flyway.enabled=true
 *   spring.flyway.url=${FLYWAY_URL:...}
 *   spring.flyway.user=${DATABASE_USER:...}
 *   spring.flyway.password=${DATABASE_PASSWORD:...}
 *   spring.flyway.locations=classpath:db/migration/common,classpath:db/migration/postgre
 *   spring.flyway.baseline-on-migrate=true
 *   spring.flyway.validate-on-migrate=true
 */
@Configuration
public class FlywayConfig {
    // Spring Boot Auto Configuration
}
