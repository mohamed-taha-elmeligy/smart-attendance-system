package com.emts.smart_attendance_system.entities;

import com.emts.smart_attendance_system.auditing.AuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.entities
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 23/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Setter
@Table(name = "academic_member")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "academicMemberId", callSuper = false)
public class AcademicMember extends AuditableEntity {

    // ===== Attributes =====
    @Id
    @ToString.Include
    @Column(value = "academic_member_id")
    private UUID academicMemberId ;

    @ToString.Include
    @Column(value = "first_name")
    private String firstName ;

    @ToString.Include
    @Column(value = "last_name")
    private String lastName ;

    @Column(value = "username")
    private String username ;

    @Column(value = "password_hash")
    private String passwordHash ;

    @ToString.Include
    @Column(value = "birthdate")
    private LocalDate birthdate ;

    @Column(value = "university_number")
    private String universityNumber ;

    @Column(value = "email")
    private String email ;

    @ToString.Include
    @Column(value = "email_verified")
    private boolean emailVerified = false;

    @Column(value = "phone")
    private String phone ;

    @Column(value = "device_id")
    private String deviceId ;

    @ToString.Include
    @Column(value = "soft_delete")
    private boolean softDelete = false;

    // ===== Relationship =====
    @Column(value = "academic_year_id")
    @ToString.Include
    private UUID academicYearId ;

    @Column(value = "role_id")
    @ToString.Include
    private UUID roleId ;

    @Column(value = "university_id")
    @ToString.Include
    private UUID universityId ;

    // ===== Builder =====
    @Builder(toBuilder = true)
    public AcademicMember (@NonNull String firstName,
                           @NonNull String lastName,
                           @NonNull String username,
                           @NonNull String passwordHash,
                           @NonNull LocalDate birthdate,
                           @NonNull String universityNumber,
                           @NonNull String email,
                           @NonNull String phone,
                           @NonNull UUID academicYearId,
                           @NonNull UUID roleId,
                           @NonNull UUID universityId,
                           @NonNull String deviceId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.birthdate = birthdate;
        this.universityNumber = universityNumber;
        this.email = email;
        this.phone = phone;
        this.deviceId = deviceId;
        this.academicYearId = academicYearId;
        this.roleId = roleId;
        this.universityId = universityId;
        this.emailVerified = false;
        this.softDelete = false;
    }

    // ===== Helper Methods for Email Verification =====
    public String getEmailVerifiedText(){
        return emailVerified ? ("Valid email "+email) : ("Invalid email "+email) ;
    }
    public boolean isValidEmail(){
        return emailVerified ;
    }
    public AcademicMember invalidateEmail(){
        this.emailVerified = false;
        return this ;
    }
    public AcademicMember validateEmail(){
        this.emailVerified = true;
        return this ;
    }

    // ===== Helper Methods for Soft Delete =====
    public String getUserDeleteStatus() {
        return softDelete
                ? "AcademicMember Deleted: " + academicMemberId
                : "AcademicMember Undeleted: " + academicMemberId;
    }

    public boolean isDeleted() {
        return softDelete;
    }

    public AcademicMember undelete() {
        this.softDelete = false;
        return this;
    }

    public AcademicMember delete() {
        this.softDelete = true;
        return this;
    }

}
