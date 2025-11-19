package com.emts.smart_attendance_system.exceptions.exception;

/**
 * *******************************************************************
 * File: CurrentDeleteException.java
 * Package: com.emts.smart_attendance_system.exceptions.exception
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 19/11/2025
 * Port Number: 8083
 * *******************************************************************
 */
public class CurrentDeleteException extends RuntimeException {
    public CurrentDeleteException(String message){
        super(message);
    }
}
