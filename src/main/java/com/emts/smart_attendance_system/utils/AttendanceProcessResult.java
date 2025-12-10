package com.emts.smart_attendance_system.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * *******************************************************************
 * File: AttendanceProcessResult.java
 * Package: com.emts.smart_attendance_system.processors
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 10/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Data
@AllArgsConstructor
public class AttendanceProcessResult {
    private int successful;
    private int failed;

    public boolean isFullySuccessful() {
        return failed == 0;
    }

    public int getTotal() {
        return successful + failed;
    }
}
