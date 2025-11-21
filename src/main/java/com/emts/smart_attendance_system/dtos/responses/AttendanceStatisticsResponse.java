package com.emts.smart_attendance_system.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.dtos.responses
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 19/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Getter
@AllArgsConstructor
public class AttendanceStatisticsResponse {
    private UUID lectureId;
    private Long total;
    private Long present;
    private Long absent;
    private Long verified;
}
