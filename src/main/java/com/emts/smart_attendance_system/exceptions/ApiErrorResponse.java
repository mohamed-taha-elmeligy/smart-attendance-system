package com.emts.smart_attendance_system.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: ApiErrorResponse.java
 * Package: com.emts.smart_attendance_system.exceptions
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Getter
public class ApiErrorResponse {
    private final int status ;
    private final String error ;
    private final String message ;
    private final String traceId ;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final String timestamp ;

    @Builder
    public ApiErrorResponse(@NonNull HttpStatus status ,
                            @NonNull String message){
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message ;
        this.traceId = UUID.randomUUID().toString();
        this.timestamp = Instant.now().toString();
    }
}
