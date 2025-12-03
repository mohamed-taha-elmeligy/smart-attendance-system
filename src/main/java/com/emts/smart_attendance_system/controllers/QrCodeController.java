package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.QrCodeConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestQrCode;
import com.emts.smart_attendance_system.dtos.responses.ResponseQrCode;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: QrCodeController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@RestController
@RequestMapping("/api/v1/qr-code")
@AllArgsConstructor
@Slf4j
public class QrCodeController {
    private QrCodeConverter qrCodeConverter;

    @PostMapping("/generate")
    public Mono<ResponseEntity<ResponseQrCode>> generateQrCode(
            @Valid @RequestBody RequestQrCode requestQrCode) {
        log.debug("Generating QR code for lecture: {}", requestQrCode.getLectureId());
        return qrCodeConverter.generateQrCode(requestQrCode)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error generating QR code: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/find-by-id")
    public Mono<ResponseEntity<ResponseQrCode>> findByIdActive(
            @RequestParam @NonNull UUID qrCodeId) {
        log.debug("Fetching active QR code by ID: {}", qrCodeId);
        return qrCodeConverter.findByIdActive(qrCodeId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> {
                    log.error("Error fetching QR code by ID: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/find-by-lecture")
    public Flux<ResponseQrCode> findByLectureId(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching active QR codes for lecture: {}", lectureId);
        return qrCodeConverter.findByLectureId(lectureId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching QR codes by lecture: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-lecture-all")
    public Flux<ResponseQrCode> findByLectureIdAll(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching all QR codes for lecture: {}", lectureId);
        return qrCodeConverter.findByLectureIdAll(lectureId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching all QR codes by lecture: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-expired-by-lecture")
    public Flux<ResponseQrCode> findExpiredByLectureId(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching expired QR codes for lecture: {}", lectureId);
        return qrCodeConverter.findExpiredByLectureId(lectureId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching expired QR codes by lecture: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @PutMapping("/expire")
    public Mono<ResponseEntity<Integer>> expireQrCode(
            @RequestParam @NonNull UUID qrCodeId) {
        log.debug("Expiring QR code: {}", qrCodeId);
        return qrCodeConverter.expireQrCode(qrCodeId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error expiring QR code: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/inactivate")
    public Mono<ResponseEntity<Integer>> inactivateQrCode(
            @RequestParam @NonNull UUID qrCodeId) {
        log.debug("Inactivating QR code: {}", qrCodeId);
        return qrCodeConverter.inactivateQrCode(qrCodeId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error inactivating QR code: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/activate")
    public Mono<ResponseEntity<Integer>> activateQrCode(
            @RequestParam @NonNull UUID qrCodeId) {
        log.debug("Activating QR code: {}", qrCodeId);
        return qrCodeConverter.activateQrCode(qrCodeId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error activating QR code: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/expire-by-lecture")
    public Mono<ResponseEntity<Integer>> expireByLectureId(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Expiring all QR codes for lecture: {}", lectureId);
        return qrCodeConverter.expireByLectureId(lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error expiring QR codes by lecture: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/count-by-lecture")
    public Mono<ResponseEntity<Long>> countByLectureId(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Counting active QR codes for lecture: {}", lectureId);
        return qrCodeConverter.countByLectureId(lectureId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting QR codes by lecture: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }
}