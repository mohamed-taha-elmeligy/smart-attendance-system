package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.exceptions.ApiErrorResponse;
import com.emts.smart_attendance_system.services.AuthService;
import com.emts.smart_attendance_system.utils.LoginRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 01/11/2025
 * Port Number: 8083
 * *******************************************************************
 */
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Object>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for username: {}", request.username());

        return authService.login(request.username(), request.password())
                .map(authResponse -> ResponseEntity.ok((Object) authResponse))
                .onErrorResume(BadCredentialsException.class, e -> {
                    log.warn("Login failed: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiErrorResponse(
                                    HttpStatus.UNAUTHORIZED,
                                    "Invalid username or password"
                            )));
                })
                .onErrorResume(IllegalStateException.class, e -> {
                    log.error("Login failed due to system error: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiErrorResponse(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "System error occurred"
                            )));
                })
                .onErrorResume(e -> {
                    log.error("Unexpected error during login: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiErrorResponse(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "An unexpected error occurred"
                            )));
                });
    }
    }
