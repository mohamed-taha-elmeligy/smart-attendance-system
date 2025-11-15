package com.emts.smart_attendance_system.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: AuthenticationManager.java
 * Package: com.emts.smart_attendance_system.security.jwt
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 28/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private PasswordEncoder passwordEncoder ;
    private ReactiveUserDetailsService reactiveUserDetailsService ;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.debug("Attempting to authenticate user: {}", username);

        return reactiveUserDetailsService.findByUsername(username)

                .switchIfEmpty(Mono.defer(() ->{
                    log.warn("Authentication failed: User not found - {}", username);
                    return Mono.error(new BadCredentialsException("Invalid username or password"));
                }))

                .flatMap(userDetails ->
                    checkAccountStatus(userDetails).flatMap(user ->{
                        if(!passwordEncoder.matches(password, user.getPassword())){
                            log.warn("Authentication failed: Invalid password for user - {}", user.getUsername());
                            return Mono.error(new BadCredentialsException("Invalid username or password"));
                        }

                        log.info("User authenticated successfully: {}", user.getUsername());
                        return Mono.just(
                                new UsernamePasswordAuthenticationToken(
                                        user.getUsername(),
                                        null,
                                        user.getAuthorities()
                                )
                        );
                    })
                );
    }

    private Mono<UserDetails> checkAccountStatus(UserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            log.warn("Account is disabled: {}", userDetails.getUsername());
            return Mono.error(new BadCredentialsException("Account is disabled"));
        }
        if (!userDetails.isAccountNonLocked()) {
            log.warn("Account is locked: {}", userDetails.getUsername());
            return Mono.error(new BadCredentialsException("Account is locked"));
        }
        if (!userDetails.isAccountNonExpired()) {
            log.warn("Account has expired: {}", userDetails.getUsername());
            return Mono.error(new BadCredentialsException("Account has expired"));
        }
        if (!userDetails.isCredentialsNonExpired()) {
            log.warn("Credentials have expired: {}", userDetails.getUsername());
            return Mono.error(new BadCredentialsException("Credentials have expired"));
        }
        return Mono.just(userDetails);
    }

}
