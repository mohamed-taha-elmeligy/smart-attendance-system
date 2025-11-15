package com.emts.smart_attendance_system.entities;

import com.emts.smart_attendance_system.auditing.AuditableEntity;
import com.emts.smart_attendance_system.enums.TypeNotification;
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
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Table(name = "notification")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "notificationId", callSuper = false)
public class Notification extends AuditableEntity {

    // ===== Attributes =====
    @Id
    @ToString.Include
    @Column("notification_id")
    private UUID notificationId ;

    @Setter
    @ToString.Include
    @Column("message")
    private String message ;

    @Setter
    @ToString.Include
    @Column("type")
    private TypeNotification type ;

    @Setter
    @ToString.Include
    @Column("is_read")
    private boolean isRead = false;

    // ===== Relationship =====
    @Setter
    @ToString.Include
    @Column(value = "academic_member_id")
    private UUID academicMemberId ;

    // ===== Builder =====
    @Builder
    public Notification (@NonNull String message,
                         @NonNull TypeNotification type,
                         @NonNull UUID academicMemberId){
        this.message = message;
        this.type = type;
        this.academicMemberId = academicMemberId;
        this.isRead = false;
    }

    // ===== Helper Methods for is Read =====
    public String getReadText() {
        return isRead
                ? "Notification Read: " + notificationId
                : "Notification Unread: " + notificationId;
    }

    public boolean isRead() {
        return isRead;
    }

    public Notification unread() {
        this.isRead = false;
        return this;
    }

    public Notification read() {
        this.isRead = true;
        return this;
    }



}
