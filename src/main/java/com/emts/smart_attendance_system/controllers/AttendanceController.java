package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.AttendanceConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestAttendance;
import com.emts.smart_attendance_system.dtos.requests.RequestQrGenerator;
import com.emts.smart_attendance_system.dtos.responses.ResponseAttendance;
import com.emts.smart_attendance_system.dtos.responses.AttendanceStatisticsResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AttendanceController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 06/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/attendance")
@Slf4j
public class AttendanceController {
    private AttendanceConverter attendanceConverter;
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";

    // ===== Mark Attendance =====
    @PutMapping("/mark-present")
    public Mono<ResponseEntity<Map<String, String>>> markPresent(
            @RequestBody @Valid RequestAttendance requestAttendance,
            @RequestBody @Valid RequestQrGenerator requestQrGenerator) {
        log.debug("Marking attendance for student: {}", requestAttendance.getStudentAcademicMemberId());

        return attendanceConverter.present(requestAttendance, requestQrGenerator)
                .flatMap(isPresent -> {
                    if (Boolean.TRUE.equals(isPresent)) {
                        log.info("Attendance marked successfully for student: {}",
                                requestAttendance.getStudentAcademicMemberId());
                        return Mono.just(ResponseEntity.ok(Map.of(
                                SUCCESS, "true",
                                MESSAGE, "Attendance marked successfully"
                        )));
                    } else {
                        log.warn("Attendance validation failed for student: {}",
                                requestAttendance.getStudentAcademicMemberId());
                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                                SUCCESS, "false",
                                MESSAGE, "Attendance validation failed"
                        )));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error marking attendance: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                            SUCCESS, "false",
                            MESSAGE, "Internal server error: " + e.getMessage()
                    )));
                });
    }

    // ===== Find Methods =====
    @GetMapping("/find-by-lecture")
    public Flux<ResponseAttendance> findByLectureId(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching attendance records for lecture: {}", lectureId);
        return attendanceConverter.findByLectureId(lectureId)
                .doOnError(e -> log.error("Error fetching attendance by lecture: {}", e.getMessage()));
    }

    @GetMapping("/find-by-student")
    public Flux<ResponseAttendance> findByStudentId(
            @RequestParam @NonNull UUID studentId) {
        log.debug("Fetching attendance records for student: {}", studentId);
        return attendanceConverter.findByStudentId(studentId)
                .doOnError(e -> log.error("Error fetching attendance by student: {}", e.getMessage()));
    }

    @GetMapping("/find-by-qr-code")
    public Flux<ResponseAttendance> findByQrCodeId(
            @RequestParam @NonNull UUID qrCodeId) {
        log.debug("Fetching attendance records for QR code: {}", qrCodeId);
        return attendanceConverter.findByQrCodeId(qrCodeId)
                .doOnError(e -> log.error("Error fetching attendance by QR code: {}", e.getMessage()));
    }

    // ===== Filter Methods =====
    @GetMapping("/find-present-in-lecture")
    public Flux<ResponseAttendance> findPresentInLecture(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching present records for lecture: {}", lectureId);
        return attendanceConverter.findPresentInLecture(lectureId)
                .doOnError(e -> log.error("Error fetching present records: {}", e.getMessage()));
    }

    @GetMapping("/find-absent-in-lecture")
    public Flux<ResponseAttendance> findAbsentInLecture(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching absent records for lecture: {}", lectureId);
        return attendanceConverter.findAbsentInLecture(lectureId)
                .doOnError(e -> log.error("Error fetching absent records: {}", e.getMessage()));
    }

    @GetMapping("/find-by-lecture-and-student")
    public Mono<ResponseEntity<ResponseAttendance>> findByLectureAndStudent(
            @RequestParam @NonNull UUID lectureId,
            @RequestParam @NonNull UUID studentId) {
        log.debug("Fetching attendance for student: {} in lecture: {}", studentId, lectureId);

        return attendanceConverter.findByLectureAndStudent(lectureId, studentId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding attendance: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    // ===== Count Methods =====
    @GetMapping("/count-present")
    public Mono<ResponseEntity<Long>> countPresentInLecture(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Counting present students in lecture: {}", lectureId);

        return attendanceConverter.countPresentInLecture(lectureId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error counting present records: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L));
                });
    }

    @GetMapping("/count-absent")
    public Mono<ResponseEntity<Long>> countAbsentInLecture(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Counting absent students in lecture: {}", lectureId);

        return attendanceConverter.countAbsentInLecture(lectureId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error counting absent records: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L));
                });
    }

    // ===== Time Range Methods =====
    @GetMapping("/find-by-time-range")
    public Flux<ResponseAttendance> findByTimeRange(
            @RequestParam @NonNull UUID lectureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {
        log.debug("Fetching attendance between {} and {}", startTime, endTime);

        return attendanceConverter.findByTimeRange(lectureId, startTime, endTime)
                .doOnError(e -> log.error("Error fetching by time range: {}", e.getMessage()));
    }

    // ===== Device Methods =====
    @GetMapping("/find-by-device")
    public Flux<ResponseAttendance> findByDeviceId(
            @RequestParam @NotBlank String deviceId) {
        log.debug("Fetching attendance records for device: {}", deviceId);

        return attendanceConverter.findByDeviceId(deviceId)
                .doOnError(e -> log.error("Error fetching by device ID: {}", e.getMessage()));
    }

    // ===== Student History Methods =====
    @GetMapping("/find-latest")
    public Mono<ResponseEntity<ResponseAttendance>> findLatestAttendanceForStudent(
            @RequestParam @NonNull UUID studentId,
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching latest attendance for student: {} in lecture: {}", studentId, lectureId);

        return attendanceConverter.findLatestAttendanceForStudentInLecture(studentId, lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error fetching latest attendance: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/find-student-history")
    public Flux<ResponseAttendance> findStudentAttendanceHistory(
            @RequestParam @NonNull UUID studentId,
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("Fetching attendance history for student: {} with limit: {}", studentId, limit);

        return attendanceConverter.findStudentAttendanceHistory(studentId, limit)
                .doOnError(e -> log.error("Error fetching student history: {}", e.getMessage()));
    }

    // ===== Check-in Methods =====
    @GetMapping("/check-in-status")
    public Mono<ResponseEntity<Boolean>> hasCheckedIn(
            @RequestParam @NonNull UUID studentId,
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Checking if student: {} has checked in for lecture: {}", studentId, lectureId);

        return attendanceConverter.hasCheckedIn(studentId, lectureId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error checking check-in status: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false));
                });
    }

    // ===== Mark Present/Absent Methods =====
    @PutMapping("/mark-as-present")
    public Mono<ResponseEntity<ResponseAttendance>> markAsPresent(
            @RequestParam @NonNull UUID studentId,
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Marking student: {} as present in lecture: {}", studentId, lectureId);

        return attendanceConverter.markAsPresent(studentId, lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)))
                .onErrorResume(e -> {
                    log.error("Error marking as present: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                });
    }

    @PutMapping("/mark-as-absent")
    public Mono<ResponseEntity<ResponseAttendance>> markAsAbsent(
            @RequestParam @NonNull UUID studentId,
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Marking student: {} as absent in lecture: {}", studentId, lectureId);

        return attendanceConverter.markAsAbsent(studentId, lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)))
                .onErrorResume(e -> {
                    log.error("Error marking as absent: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                });
    }

    // ===== Statistics Methods =====
    @GetMapping("/get-statistics")
    public Mono<ResponseEntity<AttendanceStatisticsResponse>> getAttendanceStatistics(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Fetching attendance statistics for lecture: {}", lectureId);

        return attendanceConverter.getAttendanceStatistics(lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error fetching statistics: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}