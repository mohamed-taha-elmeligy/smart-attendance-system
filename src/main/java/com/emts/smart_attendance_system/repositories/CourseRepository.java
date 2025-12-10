package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.Course;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 19/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

public interface CourseRepository extends ReactiveCrudRepository<Course, UUID> {

    // ===== Basic Finders =====
    Mono<Course> findByCodeAndSoftDeleteFalse(String code);

    Flux<Course> findByUniversityIdAndSoftDeleteFalse(UUID universityId);

    Flux<Course> findByAcademicYearIdAndSoftDeleteFalse(UUID academicYearId);

    // ===== Search =====
    Flux<Course> findByNameContainingIgnoreCaseAndSoftDeleteFalse(String name);

    Flux<Course> findByCodeContainingIgnoreCaseAndSoftDeleteFalse(String code);

    @Query("""
           SELECT * FROM course 
           WHERE 
               (LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(code) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%')))
               AND soft_delete = FALSE
           """)
    Flux<Course> search(String keyword);


    // ===== Check duplicates =====
    Mono<Boolean> existsByCodeAndSoftDeleteFalse(String code);

    // ===== Soft Delete Operations =====
    @Query("UPDATE course SET soft_delete = TRUE WHERE course_id = :courseId RETURNING *")
    Mono<Course> softDelete(UUID courseId);

    @Query("UPDATE course SET soft_delete = FALSE WHERE course_id = :courseId RETURNING *")
    Mono<Course> restore(UUID courseId);

    // ===== Active (Not Deleted) =====
    @Query("SELECT * FROM course WHERE soft_delete = FALSE")
    Flux<Course> findActive();

    // ===== Deleted =====
    @Query("SELECT * FROM course WHERE soft_delete = TRUE")
    Flux<Course> findNotDeleted();

    // ===== Filter by both university + year =====
    @Query("""
       SELECT * FROM course
       WHERE university_id = :universityId
       AND academic_year_id = :academicYearId
       AND soft_delete = FALSE
       """)
    Flux<Course> findByUniversityAndYearAndSoftDeleteFalse(UUID universityId, UUID academicYearId);
    Flux<Course> findByInstructorAcademicMemberIdAndSoftDeleteFalse(UUID instructor);
    Mono<Boolean> existsByCodeAndUniversityIdAndAcademicYearIdAndSoftDeleteFalse(
            String code, UUID universityId, UUID academicYearId);


}

