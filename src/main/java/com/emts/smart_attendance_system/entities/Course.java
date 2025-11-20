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
@Table(name = "course")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "courseId", callSuper = false)
public class Course extends AuditableEntity {

    // ===== Attributes =====
    @Id
    @ToString.Include
    @Column("course_id")
    private UUID courseId ;

    @ToString.Include
    @Column("code")
    private String code ;

    @ToString.Include
    @Column("name")
    private String name ;

    @Column("description")
    private String description ;

    @ToString.Include
    @Column("soft_delete")
    private boolean softDelete = false;

    // ===== Relationships =====
    @ToString.Include
    @Column("university_id")
    private UUID universityId ;

    @ToString.Include
    @Column("academic_year_id")
    private UUID academicYearId ;

    // ===== Builder =====
    @Builder
    public Course (@NonNull String code,
                   @NonNull String name,
                   @NonNull String description,
                   @NonNull UUID universityId,
                   @NonNull UUID academicYearId){
        this.code = code;
        this.name = name;
        this.description = description;
        this.universityId = universityId;
        this.academicYearId = academicYearId;
        this.softDelete = false;
    }

    // ===== Helper Methods for Soft Delete =====
    public String getCourseDeleteStatus() {
        return softDelete ? "Course Deleted: " + courseId : "Course Undeleted: " + courseId;
    }

    public boolean isDeleted() {
        return softDelete;
    }

    public Course undelete() {
        this.softDelete = false;
        return this;
    }

    public Course delete() {
        this.softDelete = true;
        return this;
    }
}
