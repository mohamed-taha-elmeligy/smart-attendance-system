package com.emts.smart_attendance_system.entities;

import com.emts.smart_attendance_system.auditing.AuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * *******************************************************************
 * File: Lecture.java
 * Package: com.emts.smart_attendance_system.entities
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Setter
@Table(name = "lecture")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(of = "lectureId", callSuper = false)
public class Lecture extends AuditableEntity {

    @Id
    @ToString.Include
    @Column("lecture_id")
    private UUID lectureId ;

    @ToString.Include
    @Column("lecture_date")
    private LocalDate lectureDate ;

    @ToString.Include
    @Column("day_of_week")
    private DayOfWeek dayOfWeek ;

    @ToString.Include
    @Column("start_time")
    private LocalTime startTime ;

    @ToString.Include
    @Column("end_time")
    private LocalTime endTime ;

    @ToString.Include
    @Column("room")
    private String room ;

    @ToString.Include
    @Column("status")
    private boolean status = true;

    @ToString.Include
    @Column("soft_delete")
    private boolean softDelete = false;

    // ===== Relationships =====
    @ToString.Include
    @Column("course_id")
    private UUID courseId ;

    @ToString.Include
    @Column("instructor_academic_member_id")
    private UUID instructorAcademicMemberId;

    // ===== Builder =====
    @Builder
    public Lecture (@NonNull LocalDate lectureDate,
                    @NonNull DayOfWeek dayOfWeek,
                    @NonNull LocalTime startTime,
                    @NonNull LocalTime endTime,
                    @NonNull String room,
                    UUID courseId,
                    UUID instructorAcademicMemberId){

        this.lectureDate = lectureDate;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.courseId = courseId;
        this.instructorAcademicMemberId = instructorAcademicMemberId;
        this.softDelete = false;
    }


    // ===== Helper Methods for Status =====
    public String getStatusText() {
        return status ? "Lecture Active: " + lectureId : "Lecture Inactive: " + lectureId;
    }

    public boolean isActive() {
        return status;
    }

    public Lecture inactive() {
        this.status = false;
        return this;
    }

    public Lecture active() {
        this.status = true;
        return this;
    }


    // ===== Helper Methods for Soft Delete =====
    public String getLectureDeleteStatus() {
        return softDelete ? "Lecture Deleted: " + lectureId : "Lecture Undeleted: " + lectureId;
    }

    public boolean isDeleted() {
        return softDelete;
    }

    public Lecture undelete() {
        this.softDelete = false;
        return this;
    }

    public Lecture delete() {
        this.softDelete = true;
        return this;
    }
}
