package com.emts.smart_attendance_system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 ********************************************************************
 * File: RoleData.java
 * Package: com.emts.smart_attendance_system.enums
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 30/10/2025
 * Port Number: 8083
 ********************************************************************
 */

@Getter
@AllArgsConstructor
public enum RoleData {
    ADMIN("System administrator"),
    USER("Regular user"),
    TEACHER("Teacher in the system"),
    STUDENT("Student in the system"),
    DEVELOPER("System developer");

    private final String description;
}
