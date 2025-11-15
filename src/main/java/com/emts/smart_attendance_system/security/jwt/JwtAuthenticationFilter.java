package com.emts.smart_attendance_system.security.jwt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * *******************************************************************
 * File: JwtAuthenticationFilter.java
 * Package: com.emts.smart_attendance_system.security.jwt
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 28/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh",
            "/swagger-ui",
            "/v3/api-docs",
            "/actuator/health"
    );

    private boolean shouldSkipAuthentication(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // Delay execution until subscription time to avoid eagerness
        return Mono.defer(() -> {
            String path = exchange.getRequest().getURI().getPath();

            if (shouldSkipAuthentication(path)) {
                log.debug("Skipping JWT authentication for public path: {}", path);
                return chain.filter(exchange);
            }

            String token = extractToken(exchange.getRequest());

            if (!StringUtils.hasText(token)) {
                log.debug("No JWT token found in request to: {}", path);
                // No token: proceed without authentication (Security will later handle access decision)
                return chain.filter(exchange);
            }

            // Validate token then obtain Authentication and place it in Reactor context
            return jwtTokenProvider.validateToken(token)
                    // only proceed if valid == true
                    .filter(Boolean::booleanValue)
                    .flatMap(valid -> jwtTokenProvider.getAuthentication(token))
                    .flatMap(authentication -> {
                        log.debug("Successfully authenticated user: {} for request to: {}",
                                authentication.getName(),
                                path);
                        // attach authentication into Reactor Context for downstream security checks
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    })
                    // if validation failed or auth extraction empty -> continue chain without auth
                    .switchIfEmpty(Mono.defer(() -> {
                        log.warn("Invalid token or could not extract authentication for request to: {}", path);
                        return unauthorizedResponse(exchange);
                    }))
                    .onErrorResume(error -> {
                        log.error("Error during JWT authentication for request to: {}, Error: {}", path, error.getMessage());
                        return unauthorizedResponse(exchange);
                    });

        });
    }

    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(BEARER_PREFIX_LENGTH);
            log.debug("Extracted JWT token from Authorization header");
            return token;
        }

        log.debug("No Bearer token found in Authorization header");
        return null;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = "Invalid or expired token".getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }

}
