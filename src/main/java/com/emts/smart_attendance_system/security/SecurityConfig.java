package com.emts.smart_attendance_system.security;

import com.emts.smart_attendance_system.security.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * *******************************************************************
 * File: SecurityConfig.java
 * Package: com.emts.smart_attendance_system.security
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 10/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final ServerAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ServerAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ReactiveAuthenticationManager authenticationManager;

    private static final String PROFESSOR = "PROFESSOR";
    private static final String STUDENT = "STUDENT";

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()) )
                .authorizeExchange(exchange -> exchange
                        // Public endpoints
                        .pathMatchers("/api/v1/auth/**").permitAll()

                        // Swagger/OpenAPI endpoints
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .pathMatchers("/v3/api-docs/**", "/api-docs/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()

                        // Health check
                        .pathMatchers("/actuator/health").permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(authenticationManager)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exp ->
                    exp.accessDeniedHandler(jwtAccessDeniedHandler).
                            authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of(
                // Local Development
                "http://localhost:8083",
                "http://localhost:3000",      // React
                "http://localhost:5173",      // Vue
                "http://localhost:4200",      // Angular
                "http://127.0.0.1:*",

                // Production - الموقع الفعلي
                "https://mis.kfs-hiet.edu.eg",
                "https://admin.mis.kfs-hiet.edu.eg",

                // Railway URLs
                "http://smart-attendance-system-production-253e.up.railway.app",
                "https://smart-attendance-system-production-253e.up.railway.app",
                "http://smart-attendance-system-production-253e.up.railway.app:*",
                "https://smart-attendance-system-production-253e.up.railway.app:*"
        ));

        corsConfiguration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Content-Type", "X-Total-Count"));
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
