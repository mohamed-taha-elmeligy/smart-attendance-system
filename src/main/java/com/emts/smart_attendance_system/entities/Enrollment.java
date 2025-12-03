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
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Setter
@Table(name = "enrollment")
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = "enrollmentId", callSuper = false)
public class Enrollment extends AuditableEntity {

    // ===== Attributes =====
    @Id
    @Column("enrollment_id")
    private UUID enrollmentId ;

    @Column("soft_delete")
    private boolean softDelete = false;

    // ===== Relationships =====
    @Column("course_id")
    private UUID courseId ;

    @Column("student_academic_member")
    private UUID studentAcademicMember ;

    // ===== Builder =====
    @Builder
    public Enrollment(@NonNull UUID courseId, @NonNull UUID studentAcademicMember){
        this.courseId = courseId;
        this.studentAcademicMember = studentAcademicMember;
        this.softDelete = false;
    }

    // ===== Helper Methods for Soft Delete =====
    public String getEnrollmentDeleteStatus() {
        return softDelete ? "Enrollment Deleted: " + enrollmentId : "Enrollment Undeleted: " + enrollmentId;
    }

    public boolean isDeleted() {
        return softDelete;
    }

    public Enrollment undelete() {
        this.softDelete = false;
        return this;
    }

    public Enrollment delete() {
        this.softDelete = true;
        return this;
    }}
