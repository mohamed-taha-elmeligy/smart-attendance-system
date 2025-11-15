package com.emts.smart_attendance_system.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *******************************************************************
 * File: BatchResponse.java
 * Package: com.emts.smart_attendance_system.dtos.requests
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
public class BatchResponse {
    private long successCount;
    private long failedCount;
    private long totalCount;
    private String message;

    public BatchResponse(long successCount, long failedCount, String message) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.totalCount = successCount + failedCount;
        this.message = message;
    }
}
