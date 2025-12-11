package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.security.jwt.JwtTokenProvider;
import com.emts.smart_attendance_system.utils.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * *******************************************************************
 * File: AuthService.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 01/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final AcademicMemberService academicMemberService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Mono<AuthResponse> login(String username, String password) {
        log.info("Login attempt for username: {}", username);

        return academicMemberService.findByUsername(username)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Login failed: User not found - {}", username);
                    return Mono.error(new BadCredentialsException("Invalid username or password"));
                }))
                .flatMap(academicMember -> {
                    // Verify password
                    if (!passwordEncoder.matches(password, academicMember.getPasswordHash())) {
                        log.warn("Login failed: Invalid password for username - {}", username);
                        return Mono.error(new BadCredentialsException("Invalid username or password"));
                    }

                    // Get user role
                    return roleService.findById(academicMember.getRoleId())
                            .switchIfEmpty(Mono.defer(() -> {
                                log.error("Login failed: Role not found for user {} with roleId: {}",
                                        username, academicMember.getRoleId());
                                return Mono.error(new IllegalStateException("User role not found"));
                            }))
                            .flatMap(role -> {
                                // Create authentication token
                                Authentication authentication = new UsernamePasswordAuthenticationToken(
                                        academicMember.getUsername(),
                                        null,
                                        List.of(new SimpleGrantedAuthority(role.getName()))
                                );

                                return jwtTokenProvider.generateAccessToken(authentication)
                                        .map(token -> {
                                            log.info("✓ Login successful for user: {} with role: {}",
                                                    username, role.getName());
                                            return new AuthResponse(
                                                    token,
                                                    null, // refresh token
                                                    "Bearer", // token type
                                                    3600000L, // expires in
                                                    new AuthResponse.UserInfo(
                                                            academicMember.getUsername(),
                                                            academicMember.getAcademicMemberId(),
                                                            role.getName(),
                                                            academicMember.getEmail()
                                                    )
                                            );
                                        });
                            });
                })
                .doOnError(error -> {
                    if (!(error instanceof BadCredentialsException)) {
                        log.error("✗ Login error for username {}: {}", username, error.getMessage());
                    }
                });
    }
}