package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.dtos.responses.AttendanceStatisticsResponse;
import com.emts.smart_attendance_system.entities.Attendance;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AttendanceRepository.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 19/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Repository
public interface AttendanceRepository extends R2dbcRepository<Attendance, UUID> {

    // ===== Find By Lecture =====
    Flux<Attendance> findByLectureId(UUID lectureId);

    // ===== Find By Student =====
    Flux<Attendance> findByStudentAcademicMemberId(UUID studentAcademicMemberId);

    // ===== Find By QR Code =====
    Flux<Attendance> findByQrCodeId(UUID qrCodeId);

    // ===== Find Present Attendance =====
    Flux<Attendance> findByLectureIdAndIsPresent(UUID lectureId, boolean isPresent);

    // ===== Find Attendance by Lecture and Student =====
    Mono<Attendance> findByLectureIdAndStudentAcademicMemberId(UUID lectureId, UUID studentAcademicMemberId);

    // ===== Count Present in Lecture =====
    @Query("""
            SELECT COUNT(*) FROM attendance
            WHERE lecture_id = :lectureId AND is_present = true
            """)
    Mono<Long> countPresentInLecture(@Param("lectureId") UUID lectureId);

    // ===== Count Absent in Lecture =====
    @Query("""
            SELECT COUNT(*) FROM attendance
            WHERE lecture_id = :lectureId AND is_present = false
            """)
    Mono<Long> countAbsentInLecture(@Param("lectureId") UUID lectureId);

    // ===== Find Attendance by Time Range =====
    @Query("""
            SELECT * FROM attendance
            WHERE lecture_id = :lectureId
            AND check_in_time BETWEEN :startTime AND :endTime
            """)
    Flux<Attendance> findByLectureIdAndTimeRange(
            @Param("lectureId") UUID lectureId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    // ===== Find by Device ID =====
    Flux<Attendance> findByDeviceId(String deviceId);

    // ===== Find Student Attendance in Lecture             ORDER BY check_in_time DESC =====
    @Query("""
            SELECT * FROM attendance
            WHERE student_academic_member_id = :studentId
            AND lecture_id = :lectureId
            LIMIT 1
            """)
    Mono<Attendance> findLatestAttendanceForStudentInLecture(
            @Param("studentId") UUID studentId,
            @Param("lectureId") UUID lectureId
    );

    @Query("""
           SELECT * FROM attendance
           WHERE device_id = :deviceId
           AND lecture_id = :lectureId
           LIMIT 1
           """)
    Mono<Attendance> findByDeviceIdAndLectureId(String deviceId, UUID lectureId);


    // ===== Find Student Attendance History =====
    @Query("""
            SELECT * FROM attendance
            WHERE student_academic_member_id = :studentId
            ORDER BY check_in_time DESC
            LIMIT :limit
            """)
    Flux<Attendance> findStudentAttendanceHistory(
            @Param("studentId") UUID studentId,
            @Param("limit") int limit
    );

    // ===== Check if Student Already Checked In =====
    @Query("""
            SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
            FROM attendance
            WHERE student_academic_member_id = :studentId
            AND lecture_id = :lectureId
            """)
    Mono<Boolean> existsByStudentAndLecture(
            @Param("studentId") UUID studentId,
            @Param("lectureId") UUID lectureId
    );


    // ===== Get Attendance Statistics by Lecture =====
    @Query("""
        SELECT
            lecture_id as lectureId,
            COUNT(*) as total,
            SUM(CASE WHEN is_present = true THEN 1 ELSE 0 END) as present,
            SUM(CASE WHEN is_present = false THEN 1 ELSE 0 END) as absent,
            SUM(CASE WHEN location_verified = true THEN 1 ELSE 0 END) as verified
        FROM attendance
        WHERE lecture_id = :lectureId
        GROUP BY lecture_id
        """)
    Mono<AttendanceStatisticsResponse> getAttendanceStatistics(@Param("lectureId") UUID lectureId);
}