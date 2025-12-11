package com.emts.smart_attendance_system.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * *******************************************************************
 * File: AuthResponse.java
 * Package: com.emts.smart_attendance_system.utils
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 30/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

public record AuthResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn,
        @JsonProperty("user") UserInfo user
) {
    // Default constructor with Bearer token type
    public AuthResponse(String accessToken, String refreshToken, long expiresIn, UserInfo user) {
        this(accessToken, refreshToken, "Bearer", expiresIn, user);
    }

    // Nested UserInfo record
    public record UserInfo(
            String username,
            UUID userId ,
            String role,
            String email
    ) {}
}