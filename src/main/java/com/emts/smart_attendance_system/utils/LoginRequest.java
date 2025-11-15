package com.emts.smart_attendance_system.utils;

import jakarta.validation.constraints.NotBlank;

/**
 ********************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.utils
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 01/11/2025
 * Port Number: 8083
 ********************************************************************
 */
public record LoginRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {}
