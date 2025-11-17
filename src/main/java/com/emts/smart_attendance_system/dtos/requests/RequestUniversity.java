package com.emts.smart_attendance_system.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *******************************************************************
 * File: RequestUniversity.java
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
public class RequestUniversity {

    @NotBlank(message = "University name is required")
    @Size(min = 3, max = 100, message = "University name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Location is required")
    @Size(min = 3, max = 150, message = "Location must be between 3 and 150 characters")
    private String location;
}