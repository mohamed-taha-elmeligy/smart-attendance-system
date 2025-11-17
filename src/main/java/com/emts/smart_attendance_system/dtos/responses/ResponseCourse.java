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
 * File: ResponseCourse.java
 * Package: com.emts.smart_attendance_system.dtos.responses
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
public class ResponseCourse {

    private UUID courseId;

    private String code;

    private String name;

    private String description;

    private boolean softDelete;

    private String courseDeleteStatus;

    private UUID universityId;

    private UUID academicYearId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;
}