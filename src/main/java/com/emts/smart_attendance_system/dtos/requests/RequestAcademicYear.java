package com.emts.smart_attendance_system.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *******************************************************************
 * File: RequestAcademicYear.java
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
public class RequestAcademicYear {

    @NotBlank(message = "Academic year code is required")
    @Size(min = 2, max = 20, message = "Academic year code must be between 2 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9-/]+$", message = "Academic year code must contain only uppercase letters, numbers, hyphens, and slashes")
    private String code;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;
}