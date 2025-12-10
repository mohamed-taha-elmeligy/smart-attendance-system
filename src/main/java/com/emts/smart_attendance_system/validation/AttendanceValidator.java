package com.emts.smart_attendance_system.validation;

import com.emts.smart_attendance_system.dtos.requests.RequestAttendance;
import com.emts.smart_attendance_system.dtos.requests.RequestQrGenerator;
import com.emts.smart_attendance_system.repositories.AttendanceRepository;
import com.emts.smart_attendance_system.services.QrCodeService;
import com.emts.smart_attendance_system.utils.QrTokenGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AttendanceValidator.java
 * Package: com.emts.smart_attendance_system.validation
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 04/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@Slf4j
@Component
@Transactional
public class AttendanceValidator {

    private QrCodeService qrCodeService;
    private QrTokenGenerator qrTokenGenerator;
    private AttendanceRepository attendanceRepository;

    public Mono<Boolean> attendanceValidation(RequestAttendance requestAttendance, RequestQrGenerator requestQrGenerator){
        UUID studentId = requestAttendance.getStudentAcademicMemberId();
        String deviceId = requestAttendance.getDeviceId();
        UUID qrCodeId = requestQrGenerator.getQrCodeId();
        UUID lectureId = requestAttendance.getLectureId();

        return attendanceRepository.findLatestAttendanceForStudentInLecture(studentId, lectureId)
                .hasElement()
                .flatMap(studentExists -> {
//                    if (Boolean.FALSE.equals(studentExists)) {
//                        log.warn("Student not found or invalid: {}", studentId);
//                        return Mono.just(false);
//                    }

                    return checkDeviceId(deviceId, studentId,lectureId);
                })
                .flatMap(deviceValid -> {
//                    if (Boolean.FALSE.equals(deviceValid)) {
//                        log.warn("Device validation failed: {}", deviceId);
//                        return Mono.just(false);
//                    }

                    return validateQrCode(qrCodeId, requestQrGenerator, requestAttendance);
                });
    }

    private Mono<Boolean> checkDeviceId(String deviceId, UUID studentId,UUID lectureId) {
        return attendanceRepository.findByDeviceIdAndLectureId(deviceId, lectureId)
                .doOnSuccess(find ->
                        log.info("Finding attendance with deviceId and lectureId ID: {}", find.getAttendanceId())
                )
                .map(attendance -> {
//                    boolean isSameStudent = attendance.getStudentAcademicMemberId().equals(studentId);
//                    if (!isSameStudent) {
//                        log.warn("Device already used by another student: {}", deviceId);
//                        return false;
//                    }
                    return true;
                })
                .onErrorReturn(true);
    }

    private Mono<Boolean> validateQrCode(UUID qrCodeId, RequestQrGenerator requestQrGenerator, RequestAttendance requestAttendance) {
        return qrCodeService.findByIdActive(qrCodeId)
                .flatMap(qrCode -> {
//                    boolean tokenMatches = qrTokenGenerator.matches(
//                            requestQrGenerator.getUuidTokenHash(),
//                            qrCode.getUuidTokenHash());
//
//                    if (!tokenMatches) {
//                        log.warn("Token hash mismatch for QR Code: {}", qrCodeId);
//                        return Mono.just(false);
//                    }
//
//                    if (qrCode.getExpiresAt().isBefore(Instant.now())) {
//                        log.warn("QR Code expired: {}", qrCodeId);
//                        return qrCodeService.expireQrCode(qrCode.getQrCodeId())
//                                .then(Mono.just(false));
//                    }
//
//                    boolean ipMatches = qrCode.getNetworkInfo().matches(requestAttendance.getIpAddress());
//                    if (!ipMatches) {
//                        log.warn("IP address mismatch for QR Code: {}", qrCodeId);
//                        return Mono.just(false);
//                    }

                    log.info("Attendance validated successfully for student: {}", requestAttendance.getStudentAcademicMemberId());
                    return Mono.just(true);

                })
                .onErrorReturn(false);
    }
}