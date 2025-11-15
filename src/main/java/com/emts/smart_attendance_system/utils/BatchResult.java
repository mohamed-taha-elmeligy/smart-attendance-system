package com.emts.smart_attendance_system.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *******************************************************************
 * File: BatchResult.java
 * Package: com.emts.smart_attendance_system.utils
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 14/11/2025
 * Port Number: 8083
 * *******************************************************************
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchResult {
    private long successCount;
    private long failedCount;
}
