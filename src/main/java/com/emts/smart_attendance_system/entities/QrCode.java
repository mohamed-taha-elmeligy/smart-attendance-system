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
 * File: null.java
 * Package: com.emts.smart_attendance_system.entities
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Table(name = "qr_code")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "qrCodeId", callSuper = false)
public class QrCode extends AuditableEntity {

    // ===== Attributes =====
    @Id
    @ToString.Include
    @Column("qr_code_id")
    private UUID qrCodeId ;

    @Setter
    @ToString.Include
    @Column("uuid_token_hash")
    private String uuidTokenHash ;

    @Setter
    @ToString.Include
    @Column("network_info")
    private String networkInfo ;

    @Setter
    @ToString.Include
    @Column("expires_at")
    private Instant expiresAt ;

    @Setter
    @ToString.Include
    @Column("duration_seconds")
    private long durationSeconds ;

    @Setter
    @ToString.Include
    @Column("activated")
    private boolean activated = true;

    @Column("expired")
    @ToString.Include
    private boolean expired = false;

    // ===== Relationships =====
    @Setter
    @ToString.Include
    @Column("lecture_id")
    private UUID lectureId ;


    // ===== Builder =====
    @Builder
    public QrCode (@NonNull String uuidTokenHash,
                   @NonNull String networkInfo,
                   long durationSeconds,
                   @NonNull UUID lectureId){
        this.uuidTokenHash = uuidTokenHash;
        this.networkInfo = networkInfo;
        this.durationSeconds = durationSeconds;
        this.expiresAt = Instant.now().plusSeconds(durationSeconds);
        this.lectureId = lectureId;
        this.activated = true;
        this.expired = false;
    }

    public QrCode expire() {
        this.expired = true;
        this.activated = false;
        return this;
    }

    public boolean isExpired() {
        return expired || Instant.now().isAfter(expiresAt);
    }


    // ===== Helper Methods for Activated =====
    public String getActivatedStatus() {
        return activated ? "QR Code Activated: " + qrCodeId : "QR Code Inactivated: " + qrCodeId;
    }

    public boolean isActivated() {
        return activated;
    }

    public QrCode inactivated() {
        this.activated = false;
        return this;
    }

    public QrCode activated() {
        this.activated = true;
        return this;
    }
}
