package com.emts.smart_attendance_system.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * *******************************************************************
 * File: RequestQrCode.java
 * Package: com.emts.smart_attendance_system.dtos.requests
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestQrCode {

    @NotBlank(message = "UUID Token Hash is required")
    @Size(min = 10, max = 255, message = "UUID Token Hash must be between 10 and 255 characters")
    private String uuidTokenHash;

    @NotBlank(message = "Network Info is required")
    @Size(min = 5, max = 255, message = "Network Info must be between 5 and 255 characters")
    private String networkInfo;

    @NotNull(message = "Duration in seconds is required")
    @Positive(message = "Duration must be a positive number")
    @Max(value = 3600, message = "Duration cannot exceed 1 hour (3600 seconds)")
    private long durationSeconds;

    @NotNull(message = "Lecture ID is required")
    private UUID lectureId;
}
