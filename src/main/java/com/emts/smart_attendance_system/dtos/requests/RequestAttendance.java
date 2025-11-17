package com.emts.smart_attendance_system.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * *******************************************************************
 * File: RequestAttendance.java
 * Package: com.emts.smart_attendance_system.dtos.requests
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAttendance {

    @NotBlank(message = "IP address is required")
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^(::1|([0-9a-fA-F]{0,4}:){1,7}[0-9a-fA-F]{0,4})$",
            message = "Invalid IP address format")
    private String ipAddress;

    @NotBlank(message = "Device ID is required")
    @Size(min = 5, max = 100, message = "Device ID must be between 5 and 100 characters")
    private String deviceId;

    @NotNull(message = "Lecture ID is required")
    private UUID lectureId;

    @NotNull(message = "QR Code ID is required")
    private UUID qrCodeId;

    @NotNull(message = "Student Academic Member ID is required")
    private UUID studentAcademicMemberId;
}