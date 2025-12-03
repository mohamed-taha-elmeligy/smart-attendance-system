package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.Enrollment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: EnrollmentRepository.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Repository
public interface EnrollmentRepository extends R2dbcRepository<Enrollment, UUID> {

    // ===== Find Active Enrollments =====
    /**
     * Find all active (non-deleted) enrollments
     */
    @Query("SELECT * FROM enrollment WHERE soft_delete = false")
    Flux<Enrollment> findAllActive();

    /**
     * Find active enrollment by ID
     */
    @Query("SELECT * FROM enrollment WHERE enrollment_id = :enrollmentId AND soft_delete = false")
    Mono<Enrollment> findByIdActive(@Param("enrollmentId") UUID enrollmentId);

    // ===== Find by Course =====
    /**
     * Find all active enrollments for a specific course
     */
    @Query("SELECT * FROM enrollment WHERE course_id = :courseId AND soft_delete = false")
    Flux<Enrollment> findByCourseId(@Param("courseId") UUID courseId);

    // ===== Find by Student =====
    /**
     * Find all active enrollments for a specific student
     */
    @Query("SELECT * FROM enrollment WHERE student_academic_member = :studentAcademicMember AND soft_delete = false")
    Flux<Enrollment> findByStudentAcademicMember(@Param("studentAcademicMember") UUID studentAcademicMember);


    // ===== Find Specific Enrollment =====
    /**
     * Find active enrollment by course and student
     */
    @Query("SELECT * FROM enrollment WHERE course_id = :courseId AND student_academic_member = :studentAcademicMember AND soft_delete = false")
    Mono<Enrollment> findByExistsActive(@Param("courseId") UUID courseId, @Param("studentAcademicMember") UUID studentAcademicMember);

    /**
     * Check if enrollment exists for course and student (including deleted)
     */
    @Query("SELECT * FROM enrollment WHERE course_id = :courseId AND student_academic_member = :studentAcademicMember")
    Mono<Enrollment> findByExists(@Param("courseId") UUID courseId, @Param("studentAcademicMember") UUID studentAcademicMember);

    // ===== Count Operations =====
    /**
     * Count active enrollments in a course
     */
    @Query("SELECT COUNT(*) FROM enrollment WHERE course_id = :courseId AND soft_delete = false")
    Mono<Long> countByCourseId(@Param("courseId") UUID courseId);

    /**
     * Count active enrollments for a student
     */
    @Query("SELECT COUNT(*) FROM enrollment WHERE student_academic_member = :studentAcademicMember AND soft_delete = false")
    Mono<Long> countByStudentAcademicMember(@Param("studentAcademicMember") UUID studentAcademicMember);

    /**
     * Count total active enrollments
     */
    @Query("SELECT COUNT(*) FROM enrollment WHERE soft_delete = false")
    Mono<Long> countAllActive();

    // ===== Soft Delete Operations =====
    /**
     * Soft delete all enrollments for a specific course
     */
    @Query("UPDATE enrollment SET soft_delete = true, modified_date = NOW() WHERE course_id = :courseId")
    Mono<Integer> softDeleteByCourseId(@Param("courseId") UUID courseId);

    /**
     * Soft delete all enrollments for a specific student
     */
    @Query("UPDATE enrollment SET soft_delete = true, modified_date = NOW() WHERE student_academic_member = :studentAcademicMember")
    Mono<Integer> softDeleteByStudentAcademicMember(@Param("studentAcademicMember") UUID studentAcademicMember);

    /**
     * Restore (undelete) a specific enrollment
     */
    @Query("UPDATE enrollment SET soft_delete = false, modified_date = NOW() WHERE enrollment_id = :enrollmentId")
    Mono<Integer> restoreEnrollment(@Param("enrollmentId") UUID enrollmentId);

    /**
     * Restore all deleted enrollments for a course
     */
    @Query("UPDATE enrollment SET soft_delete = false, modified_date = NOW() WHERE course_id = :courseId AND soft_delete = true")
    Mono<Integer> restoreByCourseId(@Param("courseId") UUID courseId);

    // ===== Find Deleted Enrollments =====
    /**
     * Find all deleted enrollments
     */
    @Query("SELECT * FROM enrollment WHERE soft_delete = true")
    Flux<Enrollment> findAllDeleted();

    /**
     * Find all deleted enrollments for a specific course
     */
    @Query("SELECT * FROM enrollment WHERE course_id = :courseId AND soft_delete = true")
    Flux<Enrollment> findDeletedByCourseId(@Param("courseId") UUID courseId);
}
