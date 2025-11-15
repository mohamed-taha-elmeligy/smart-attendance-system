package com.emts.smart_attendance_system.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.dtos.requests
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 27/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Setter
@Getter
public class RequestRole {

    @NotBlank(message = "Role name is required")
    @Size(min = 2, max = 50, message = "Role name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Role description is required")
    @Size(min = 10, max = 255, message = "Role description must be between 10 and 255 characters")
    private String description;

}
