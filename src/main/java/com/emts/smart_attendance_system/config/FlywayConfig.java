package com.emts.smart_attendance_system.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * *******************************************************************
 * File: FlywayConfig.java
 * Package: com.emts.smart_attendance_system.config
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 26/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

    /**
     * يقوم بإنشاء وتكوين Flyway Bean باستخدام بيانات من application.properties
     * يقرأ التالي من الـ Properties:
     * - spring.flyway.url
     * - spring.flyway.user
     * - spring.flyway.password
     * - spring.flyway.locations
     * - spring.flyway.baseline-on-migrate
     * - spring.flyway.baseline-version
     * - spring.flyway.clean-disabled
     */
    @Bean(initMethod = "migrate")
    public Flyway flyway(FlywayProperties flywayProperties) {
        return Flyway.configure()
                .dataSource(
                        flywayProperties.getUrl(),
                        flywayProperties.getUser(),
                        flywayProperties.getPassword()
                )
                .locations(flywayProperties.getLocations().toArray(new String[0]))
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
                .baselineVersion(flywayProperties.getBaselineVersion())
                .cleanDisabled(flywayProperties.isCleanDisabled())
                .load();
    }
}