package com.emts.smart_attendance_system.auditing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.callback
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Setter
@Getter
public abstract class AuditableEntity {
    @Column(value = "created_at")
    private Instant createdAt ;
    @Column(value = "updated_at")
    private Instant updatedAt ;

}
