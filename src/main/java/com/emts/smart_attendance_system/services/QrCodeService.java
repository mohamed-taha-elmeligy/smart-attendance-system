package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.QrCode;
import com.emts.smart_attendance_system.repositories.QrCodeRepository;
import com.emts.smart_attendance_system.utils.QrTokenGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: QrCodeService.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
@AllArgsConstructor
@Slf4j
public class QrCodeService {
    private QrCodeRepository qrCodeRepository;
    private RetryConfig retryConfig;
    private QrTokenGenerator qrTokenGenerator;


    // ===== Create =====
    public Mono<QrCode> generateQrCode( String networkInfo, long durationSeconds, UUID lectureId) {
        log.info("Generating QR code for lecture: {}", lectureId);
        String qrToken = qrTokenGenerator.generateRawToken();
        String hashedToken = qrTokenGenerator.hashToken(qrToken);
        QrCode qrCode = QrCode.builder()
                .uuidTokenHash(hashedToken)
                .networkInfo(networkInfo)
                .durationSeconds(durationSeconds)
                .lectureId(lectureId)
                .build();

        return qrCodeRepository.save(qrCode)
                .retryWhen(retryConfig.createRetrySpec("Generate QR Code"))
                .doOnSuccess(saved -> log.info("Generated QR Code ID: {}", saved.getQrCodeId()));
    }

    // ===== Find Active QR Codes =====
    public Mono<QrCode> findByIdActive(UUID qrCodeId) {
        log.debug("Fetching active QR code by ID: {}", qrCodeId);
        return qrCodeRepository.findByIdActive(qrCodeId);
    }

    // ===== Find by Lecture =====
    public Flux<QrCode> findByLectureId(UUID lectureId) {
        log.debug("Fetching active QR codes for lecture: {}", lectureId);
        return qrCodeRepository.findByLectureIdActive(lectureId);
    }

    public Flux<QrCode> findByLectureIdAll(UUID lectureId) {
        log.debug("Fetching all QR codes for lecture: {}", lectureId);
        return qrCodeRepository.findByLectureId(lectureId);
    }

    // ===== Verify Token =====
    public Mono<Boolean> verifyToken(String rawToken, UUID qrCodeId) {
        log.debug("Verifying token for QR Code: {}", qrCodeId);
        return findByIdActive(qrCodeId)
                .map(qrCode -> {
                    boolean matches = qrTokenGenerator.matches(rawToken, qrCode.getUuidTokenHash());
                    log.debug("Token verification result for QR Code {}: {}", qrCodeId, matches);
                    return matches;
                })
                .defaultIfEmpty(false)
                .onErrorReturn(false);
    }


    // ===== Find Expired/Inactive =====

    public Flux<QrCode> findExpiredByLectureId(UUID lectureId) {
        log.debug("Fetching expired QR codes for lecture: {}", lectureId);
        return qrCodeRepository.findExpiredByLectureId(lectureId);
    }

    // ===== Status Update Operations =====
    public Mono<Integer> expireQrCode(UUID qrCodeId) {
        log.info("Expiring QR Code: {}", qrCodeId);
        return qrCodeRepository.expireQrCode(qrCodeId)
                .retryWhen(retryConfig.createRetrySpec("Expire QR Code"))
                .doOnSuccess(expired -> log.info("QR Code expired: {}", qrCodeId));
    }

    public Mono<Integer> inactivateQrCode(UUID qrCodeId) {
        log.info("Inactivating QR Code: {}", qrCodeId);
        return qrCodeRepository.inactivateQrCode(qrCodeId)
                .retryWhen(retryConfig.createRetrySpec("Inactivate QR Code"))
                .doOnSuccess(count -> log.info("QR Code inactivated: {}", qrCodeId));
    }

    public Mono<Integer> activateQrCode(UUID qrCodeId) {
        log.info("Activating QR Code: {}", qrCodeId);
        return qrCodeRepository.activateQrCode(qrCodeId)
                .retryWhen(retryConfig.createRetrySpec("Activate QR Code"))
                .doOnSuccess(count -> log.info("QR Code activated: {}", qrCodeId));
    }

    public Mono<Integer> expireByLectureId(UUID lectureId) {
        log.info("Expiring all QR codes for lecture: {}", lectureId);
        return qrCodeRepository.expireByLectureId(lectureId)
                .retryWhen(retryConfig.createRetrySpec("Expire QR Codes by Lecture"))
                .doOnSuccess(count -> log.info("Expired {} QR codes for lecture: {}", count, lectureId));
    }

    // ===== Count Operations =====
    public Mono<Long> countByLectureId(UUID lectureId) {
        log.debug("Counting all QR codes for lecture: {}", lectureId);
        return qrCodeRepository.countByLectureId(lectureId)
                .doOnNext(count -> log.debug("Total QR codes for lecture {}: {}", lectureId, count));
    }
}
