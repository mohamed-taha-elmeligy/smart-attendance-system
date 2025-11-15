package com.emts.smart_attendance_system.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.dtos.responses
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAcademicMember {

    private UUID academicMemberId;

    private String firstName;

    private String lastName;

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String universityNumber;

    private String email;

    private boolean emailVerified;

    private String phone;

    private String deviceId;

    private boolean softDelete;

    private UUID academicYearId;

    private UUID roleId;

    private UUID universityId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    private String emailVerificationStatus;
}
