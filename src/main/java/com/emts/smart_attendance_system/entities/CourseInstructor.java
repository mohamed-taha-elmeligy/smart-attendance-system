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
 * © Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
@Table(name = "course_instructor")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "courseInstructorId", callSuper = false)
public class CourseInstructor extends AuditableEntity {

    // ===== Primary Key =====
    @Id
    @ToString.Include
    @Column("course_instructor_id")
    private UUID courseInstructorId;

    // ===== Relationships =====
    @Setter
    @ToString.Include
    @Column("instructor_academic_member_id")
    private UUID instructorAcademicMemberId;

    @Setter
    @ToString.Include
    @Column("course_id")
    private UUID courseId;

    // ===== Builder =====
    @Builder
    public CourseInstructor(@NonNull UUID instructorAcademicMemberId,
                            @NonNull UUID courseId) {
        this.instructorAcademicMemberId = instructorAcademicMemberId;
        this.courseId = courseId;
    }

}
