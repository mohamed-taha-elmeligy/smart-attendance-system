/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.enums
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
package com.emts.smart_attendance_system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Month;

/**
 ********************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.enums
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 ********************************************************************
 */
@Getter
@AllArgsConstructor
public enum DeveloperData {
    TEAM_LEADER(
            "Mahmoud",
            "Ibrahim",
            "TeamLeader",
            "01203258202",
            "mahmouddesouky5@gmail.com",
            LocalDate.of(2003, Month.SEPTEMBER, 3),
            "UNI-2003-001"
    ),

    TECH_TEAM_LEADER(
            "Mohamed",
            "Taha",
            "TechTeamLeader",
            "01282817089",
            "mohamed742590taha@gmail.com",
            LocalDate.of(2002, Month.JANUARY, 7),
            "UNI-2002-001"
    );

    private final String firstName;
    private final String lastName;
    private final String username;
    private final String phone;
    private final String email;
    private final LocalDate birthdate;
    private final String universityNumber;
}
