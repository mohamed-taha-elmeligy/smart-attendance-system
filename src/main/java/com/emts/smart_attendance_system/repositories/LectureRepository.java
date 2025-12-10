package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.Lecture;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 21/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
public interface LectureRepository extends ReactiveCrudRepository<Lecture, UUID> {

    // ===== Find by Course =====
    Flux<Lecture> findByCourseIdAndSoftDeleteFalse(UUID courseId);

    Flux<Lecture> findByCourseIdAndStatusTrueAndSoftDeleteFalse(UUID courseId);

    // ===== Find by Instructor =====
    Flux<Lecture> findByInstructorAcademicMemberIdAndSoftDeleteFalse(UUID instructorAcademicMemberId);

    Flux<Lecture> findByInstructorAcademicMemberIdAndStatusTrueAndSoftDeleteFalse(UUID instructorAcademicMemberId);

    // ===== Find by Course and Instructor =====
    Flux<Lecture> findByCourseIdAndInstructorAcademicMemberIdAndSoftDeleteFalse(UUID courseId, UUID instructorAcademicMemberId);

    Flux<Lecture> findByCourseIdAndInstructorAcademicMemberIdAndStatusTrueAndSoftDeleteFalse(UUID courseId, UUID instructorAcademicMemberId);

    // ===== Find by Date =====
    Flux<Lecture> findByLectureDateAndSoftDeleteFalse(LocalDate lectureDate);

    Flux<Lecture> findByLectureDateAndStatusTrueAndSoftDeleteFalse(LocalDate lectureDate);

    Flux<Lecture> findByLectureDateBetweenAndSoftDeleteFalse(LocalDate startDate, LocalDate endDate);

    Flux<Lecture> findByLectureDateBetweenAndStatusTrueAndSoftDeleteFalse(LocalDate startDate, LocalDate endDate);

    // ===== Find by Instructor and Date =====
    Flux<Lecture> findByInstructorAcademicMemberIdAndLectureDateAndSoftDeleteFalse(UUID instructorAcademicMemberId, LocalDate lectureDate);

    Flux<Lecture> findByInstructorAcademicMemberIdAndLectureDateBetweenAndSoftDeleteFalse(UUID instructorAcademicMemberId, LocalDate startDate, LocalDate endDate);

    // ===== Find by Course and Date =====
    Flux<Lecture> findByCourseIdAndLectureDateAndSoftDeleteFalse(UUID courseId, LocalDate lectureDate);

    Flux<Lecture> findByCourseIdAndLectureDateBetweenAndSoftDeleteFalse(UUID courseId, LocalDate startDate, LocalDate endDate);

    // ===== Find by Room =====
    Flux<Lecture> findByRoomAndSoftDeleteFalse(String room);

    Flux<Lecture> findByRoomAndStatusTrueAndSoftDeleteFalse(String room);

    // ===== Status Operations =====
    @Query("UPDATE lecture SET status = TRUE WHERE lecture_id = :lectureId RETURNING *")
    Mono<Lecture> activate(UUID lectureId);

    @Query("UPDATE lecture SET status = FALSE WHERE lecture_id = :lectureId RETURNING *")
    Mono<Lecture> deactivate(UUID lectureId);

    // ===== Soft Delete Operations =====
    @Query("UPDATE lecture SET soft_delete = TRUE WHERE lecture_id = :lectureId RETURNING *")
    Mono<Lecture> softDelete(UUID lectureId);

    @Query("UPDATE lecture SET soft_delete = FALSE WHERE lecture_id = :lectureId RETURNING *")
    Mono<Lecture> restore(UUID lectureId);

    // ===== Find Active =====
    @Query("SELECT * FROM lecture WHERE status = TRUE AND soft_delete = FALSE")
    Flux<Lecture> findActive();

    @Query("SELECT * FROM lecture WHERE status = FALSE AND soft_delete = FALSE")
    Flux<Lecture> findInactive();

    @Query("SELECT * FROM lecture WHERE soft_delete = TRUE")
    Flux<Lecture> findDeleted();

    Flux<Lecture> findByDayOfWeek(DayOfWeek dayOfWeek);
    Mono<Boolean> existsByLectureDateAndSoftDeleteFalse(LocalDate lectureDate);


    // ===== Count =====
    Mono<Long> countByCourseIdAndSoftDeleteFalse(UUID courseId);

    Mono<Long> countByInstructorAcademicMemberIdAndSoftDeleteFalse(UUID instructorAcademicMemberId);
}