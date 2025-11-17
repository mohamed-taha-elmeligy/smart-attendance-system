package com.emts.smart_attendance_system.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * *******************************************************************
 * File: RequestLecture.java
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
public class RequestLecture {

    @NotNull(message = "Lecture date is required")
    @FutureOrPresent(message = "Lecture date must be today or in the future")
    private LocalDate lectureDate;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Room is required")
    @Size(min = 1, max = 50, message = "Room must be between 1 and 50 characters")
    private String room;

    @NotNull(message = "Course ID is required")
    private UUID courseId;

    @NotNull(message = "Instructor Academic Member ID is required")
    private UUID instructorAcademicMemberId;
}