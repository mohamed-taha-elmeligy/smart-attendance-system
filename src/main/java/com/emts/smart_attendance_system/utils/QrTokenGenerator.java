package com.emts.smart_attendance_system.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * *******************************************************************
 * File: QrTokenGenerator.java
 * Package: com.emts.smart_attendance_system.utils
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@RequiredArgsConstructor
public class QrTokenGenerator {

    private final PasswordEncoder passwordEncoder;

    public String generateRawToken() {
        return UUID.randomUUID().toString();
    }

    public String hashToken(String raw) {
        return passwordEncoder.encode(raw);
    }

    public boolean matches(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }
}

