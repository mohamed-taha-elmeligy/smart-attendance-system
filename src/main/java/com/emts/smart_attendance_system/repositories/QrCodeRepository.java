package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.QrCode;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: QrCodeRepository.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Repository
public interface QrCodeRepository extends R2dbcRepository<QrCode, UUID> {

    /**
     * Find all active QR codes for a specific lecture
     */
    @Query("SELECT * FROM qr_code WHERE lecture_id = :lectureId AND activated = true AND expired = false")
    Flux<QrCode> findByLectureIdActive(@Param("lectureId") UUID lectureId);

    /**
     * Find all QR codes (including expired/inactive) for a specific lecture
     */
    @Query("SELECT * FROM qr_code WHERE lecture_id = :lectureId")
    Flux<QrCode> findByLectureId(@Param("lectureId") UUID lectureId);

    /**
     * Find active QR code by ID
     */
    @Query("SELECT * FROM qr_code WHERE qr_code_id = :qrCodeId AND activated = true AND expired = false")
    Mono<QrCode> findByIdActive(@Param("qrCodeId") UUID qrCodeId);

    /**
     * Find expired QR codes for a specific lecture
     */
    @Query("SELECT * FROM qr_code WHERE lecture_id = :lectureId AND expired = true")
    Flux<QrCode> findExpiredByLectureId(@Param("lectureId") UUID lectureId);

    // ===== Status Update Operations =====
    /**
     * Expire a specific QR code
     */
    @Query("UPDATE qr_code SET expired = true, activated = false, modified_date = NOW() WHERE qr_code_id = :qrCodeId")
    Mono<Integer> expireQrCode(@Param("qrCodeId") UUID qrCodeId);

    /**
     * Inactivate a specific QR code
     */
    @Query("UPDATE qr_code SET activated = false, modified_date = NOW() WHERE qr_code_id = :qrCodeId")
    Mono<Integer> inactivateQrCode(@Param("qrCodeId") UUID qrCodeId);

    /**
     * Activate a specific QR code
     */
    @Query("UPDATE qr_code SET activated = true, modified_date = NOW() WHERE qr_code_id = :qrCodeId AND expired = false")
    Mono<Integer> activateQrCode(@Param("qrCodeId") UUID qrCodeId);

    /**
     * Expire all QR codes for a specific lecture
     */
    @Query("UPDATE qr_code SET expired = true, activated = false, modified_date = NOW() WHERE lecture_id = :lectureId")
    Mono<Integer> expireByLectureId(@Param("lectureId") UUID lectureId);

    // ===== Count Operations =====

    /**
     * Count all QR codes for a lecture
     */
    @Query("SELECT COUNT(*) FROM qr_code WHERE lecture_id = :lectureId")
    Mono<Long> countByLectureId(@Param("lectureId") UUID lectureId);
}

