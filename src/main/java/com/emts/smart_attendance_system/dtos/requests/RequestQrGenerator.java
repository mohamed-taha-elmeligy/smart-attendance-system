package com.emts.smart_attendance_system.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.dtos.requests
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 03/12/2025
 * Port Number: 8083
 * *******************************************************************
 */
public class RequestQrGenerator {
    @NotNull(message = "Lecture ID is required")
    private UUID lectureId;

    @NotBlank(message = "Token hash is required")
    @Size(min = 5, max = 255, message = "Token hash must be between 5 and 255 characters")
    private String uuidTokenHash;
}
