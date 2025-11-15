package com.emts.smart_attendance_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.config
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 26/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Configuration
@EnableR2dbcAuditing
public class AuditingConfig {
    @Bean
    public ReactiveAuditorAware<String> auditorAware() {
        return () -> Mono.just("SYSTEM");
    }
}
