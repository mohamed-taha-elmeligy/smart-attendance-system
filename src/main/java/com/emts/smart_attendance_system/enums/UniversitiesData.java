package com.emts.smart_attendance_system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 ********************************************************************
 * File: UniversitiesData.java
 * Package: com.emts.smart_attendance_system.enums
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 ********************************************************************
 */

@Getter
@AllArgsConstructor
public enum UniversitiesData {
    HIET(" قسم اول مدينة كفر الشيخ، محافظة كفر الشيخ 6862030"),
    DEVELOPERS_UNIVERSITY("6 قسم سيدى جابر، الإسكندرية 5432062");
    private final String location;
}
