package com.emts.smart_attendance_system.entities;

import com.emts.smart_attendance_system.auditing.AuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.entities
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Setter
@Table(name = "university")
@Getter
@EqualsAndHashCode(callSuper = false , of = "universitiesId")
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class University extends AuditableEntity {

    // ===== Attributes =====
    @Column("universities_id")
    @ToString.Include
    @Id
    private UUID universitiesId;

    @Column("name")
    @ToString.Include
    private String name;

    @Column("location")
    @ToString.Include
    private String location;

    // ===== Builder =====
    @Builder
    public University (@NonNull String name,
                       @NonNull String location){
        this.name = name;
        this.location = location;
    }
}
