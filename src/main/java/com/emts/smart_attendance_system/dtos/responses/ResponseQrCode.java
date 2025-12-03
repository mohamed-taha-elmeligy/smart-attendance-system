package com.emts.smart_attendance_system.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: ResponseQrCode.java
 * Package: com.emts.smart_attendance_system.dtos.responses
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
public class ResponseQrCode {

    private UUID qrCodeId;

    private String uuidTokenHash;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant expiresAt;

    private long durationSeconds;

    private boolean activated;

    private String activatedStatus;

    private boolean expired;

    private UUID lectureId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;
}