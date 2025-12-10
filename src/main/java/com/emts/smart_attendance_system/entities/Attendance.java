package com.emts.smart_attendance_system.entities;

import com.emts.smart_attendance_system.auditing.AuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: Attendance.java
 * Package: com.emts.smart_attendance_system.entities
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Setter
@Table(name = "attendance")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = {"attendanceId"}, callSuper = false)
public class Attendance extends AuditableEntity {

    // ===== Attributes =====
    @Id
    @ToString.Include
    @Column("attendance_id")
    private UUID attendanceId ;

    @ToString.Include
    @Column("check_in_time")
    private Instant checkInTime ;

    @ToString.Include
    @Column("ip_address")
    private String ipAddress ;

    @ToString.Include
    @Column("device_id")
    private String deviceId ;

    @ToString.Include
    @Column("is_present")
    private boolean isPresent = false;

    @ToString.Include
    @Column("location_verified")
    private boolean locationVerified = false;

    // ===== Relationships =====
    @ToString.Include
    @Column("lecture_id")
    private UUID lectureId ;

    @ToString.Include
    @Column("qr_code_id")
    private UUID qrCodeId ;

    @ToString.Include
    @Column("student_academic_member_id")
    private UUID studentAcademicMemberId ;


    // ===== Builder =====
    @Builder
    public Attendance (@NonNull String ipAddress,
                       @NonNull String deviceId,
                       @NonNull UUID lectureId,
                       @NonNull UUID qrCodeId,
                       @NonNull UUID studentAcademicMemberId){

        this.ipAddress = ipAddress;
        this.deviceId = deviceId;
        this.checkInTime = Instant.now();

        this.lectureId = lectureId;
        this.qrCodeId = qrCodeId;
        this.studentAcademicMemberId = studentAcademicMemberId;

        this.locationVerified = false;
        this.isPresent = false;
    }

    public boolean isPresent() {
        return isPresent;
    }


    // ===== Helper Methods for Location Verification =====
    public boolean isValidLocation(){
        return locationVerified ;
    }
    public String getLocationVerifiedText() {
        return locationVerified ? "Valid Location " + ipAddress : "Invalid Location " + ipAddress;
    }

    public Attendance validateLocation() {
        this.locationVerified = true;
        return this;
    }

    public Attendance invalidateLocation() {
        this.locationVerified = false;
        return this;
    }

    // ===== Helper Methods for Present =====
    public String getPresentedText() {
        return isPresent ? "Present: " + studentAcademicMemberId : "Absent: " + studentAcademicMemberId;
    }

    public Attendance presented() {
        this.isPresent = true;
        return this;
    }

    public Attendance absent() {
        this.isPresent = false;
        return this;
    }
}
