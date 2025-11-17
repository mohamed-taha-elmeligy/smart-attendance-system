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
@Table(name = "academic_year")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "academicYearId", callSuper = false)
public class AcademicYear extends AuditableEntity {

    // ===== Attributes =====
    @Id
    @ToString.Include
    @Column("academic_year_id")
    private UUID academicYearId ;

    @ToString.Include
    @Column("code")
    private String code ;

    @Column("description")
    private String description ;

    @ToString.Include
    @Column(value = "soft_delete")
    private boolean softDelete = false;

    // ===== Builder =====
    @Builder
    public AcademicYear (@NonNull String code,
                         @NonNull String description){
        this.code = code ;
        this.description = description ;
        this.softDelete = false;
    }


    // ===== Helper Methods for Soft Delete =====
    public String getAcademicYearDeleteStatus() {
        return softDelete ? "AcademicYear Deleted: " + academicYearId : "AcademicYear Undeleted: " + academicYearId;
    }
    public boolean isDeleted() {
        return softDelete;
    }
    public AcademicYear undelete() {
        this.softDelete = false;
        return this;
    }
    public AcademicYear delete() {
        this.softDelete = true;
        return this;
    }
}
