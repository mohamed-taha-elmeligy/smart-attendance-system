package com.emts.smart_attendance_system.utils;

import lombok.*;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.utils
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 30/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class ApiResponse<T> {
    private boolean state ;
    private final String message ;
    private final T data ;

    public static <T> ApiResponse<T> success (String message , T data){
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .state(true)
                .build();
    }
    public static <T> ApiResponse<T> error (String message){
        return ApiResponse.<T>builder()
                .message(message)
                .data(null)
                .state(false)
                .build();
    }

}

