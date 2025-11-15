package com.emts.smart_attendance_system.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * *******************************************************************
 * File: JwtTokenProvider.java
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
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final ReactiveUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration;

    private static final String AUTHORITIES_KEY = "roles";
    private static final String TOKEN_TYPE_KEY = "type";
    private static final String ACCESS_TOKEN = "ACCESS";
    private static final String REFRESH_TOKEN = "REFRESH";

    public Mono<String> generateAccessToken(Authentication authentication) {
        return Mono.fromCallable(() -> {
            String username = authentication.getName();
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpiration);

            String roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            log.debug("Generating access token for user: {} with roles: {}", username, roles);

            return Jwts.builder()
                    .subject(username)
                    .claim(AUTHORITIES_KEY, roles)
                    .claim(TOKEN_TYPE_KEY, ACCESS_TOKEN)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey(), Jwts.SIG.HS512)
                    .compact();
        });
    }

    public Mono<String> generateRefreshToken(Authentication authentication) {
        return Mono.fromCallable(() -> {
            String username = authentication.getName();
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + refreshExpiration);

            log.debug("Generating refresh token for user: {}", username);

            return Jwts.builder()
                    .subject(username)
                    .claim(TOKEN_TYPE_KEY, REFRESH_TOKEN)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey(), Jwts.SIG.HS512)
                    .compact();
        });
    }

    public Mono<String> getUsernameFromToken(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        }).onErrorResume(ex -> {
            log.error("Error extracting username from token: {}", ex.getMessage());
            return Mono.empty();
        });
    }

    public Mono<Authentication> getAuthentication(String token) {
        return Mono.fromCallable(() -> {
                    Claims claims = Jwts.parser()
                            .verifyWith(getSigningKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

                    String username = claims.getSubject();
                    String rolesString = claims.get(AUTHORITIES_KEY, String.class);

                    Collection<? extends GrantedAuthority> authorities =
                            Arrays.stream(rolesString != null ? rolesString.split(",") : new String[0])
                                    .filter(role -> !role.trim().isEmpty())
                                    .map(SimpleGrantedAuthority::new)
                                    .toList();

                    return username;
                })
                .flatMap(username ->
                        userDetailsService.findByUsername(username)  // إزالة casting غير ضروري
                                .map(userDetails -> (Authentication) new UsernamePasswordAuthenticationToken(  // ← Casting هنا
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                ))
                )
                .onErrorResume(ex -> {
                    log.error("Error extracting authentication from token: {}", ex.getMessage());
                    return Mono.empty();
                });
    }
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token);
                return true;
            } catch (JwtException | IllegalArgumentException ex) {
                log.error("JWT validation failed: {}", ex.getMessage());
                return false;
            }
        });
    }

    public Mono<Boolean> isAccessToken(String token) {
        return getTokenType(token).map(ACCESS_TOKEN::equals);
    }

    public Mono<Boolean> isRefreshToken(String token) {
        return getTokenType(token).map(REFRESH_TOKEN::equals);
    }

    private Mono<String> getTokenType(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get(TOKEN_TYPE_KEY, String.class);
        }).onErrorResume(ex -> {
            log.error("Error checking token type: {}", ex.getMessage());
            return Mono.empty();
        });
    }

    public Mono<Date> getExpirationDateFromToken(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration();
        }).onErrorResume(ex -> {
            log.error("Error getting expiration date: {}", ex.getMessage());
            return Mono.empty();
        });
    }

    public Mono<Boolean> isTokenExpired(String token) {
        return getExpirationDateFromToken(token)
                .map(expiration -> expiration.before(new Date()))
                .defaultIfEmpty(true);
    }

    public Mono<Long> getRemainingTime(String token) {
        return getExpirationDateFromToken(token)
                .map(expiration -> expiration.getTime() - new Date().getTime())
                .defaultIfEmpty(0L);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}