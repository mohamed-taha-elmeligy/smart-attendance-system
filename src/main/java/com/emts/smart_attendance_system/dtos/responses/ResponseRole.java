package com.emts.smart_attendance_system.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.dtos.responses
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 27/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Setter
@Getter
public class ResponseRole {

    private UUID roleId;
    private String name;
    private String description;
    String getRoleDeleteStatus;
    private boolean softDelete;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

}
