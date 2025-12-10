package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.dtos.requests.RequestAttendance;
import com.emts.smart_attendance_system.dtos.requests.RequestQrGenerator;
import com.emts.smart_attendance_system.dtos.responses.AttendanceStatisticsResponse;
import com.emts.smart_attendance_system.entities.Attendance;
import com.emts.smart_attendance_system.repositories.AttendanceRepository;
import com.emts.smart_attendance_system.validation.AttendanceValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AttendanceService.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 19/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
@AllArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final RetryConfig retryConfig;
    private AttendanceValidator attendanceValidator;

    public Flux<Attendance> addAttendances(Flux<Attendance> attendanceFlux) {
        return attendanceFlux
                .flatMap(attendance ->
                        attendanceRepository.save(attendance)
                                .retryWhen(retryConfig.createRetrySpec("save-attendance"))
                                .onErrorResume(e -> Mono.just(attendance))
                );
    }

    public Mono<Boolean> presence(RequestAttendance requestAttendance,
                                  RequestQrGenerator requestQrGenerator){
        return attendanceValidator.attendanceValidation(requestAttendance, requestQrGenerator)
                .flatMap(validate -> {
                    if (Boolean.FALSE.equals(validate))
                        return Mono.just(false);

                    return findByLectureAndStudent(
                            requestAttendance.getLectureId(),
                            requestAttendance.getStudentAcademicMemberId())
                            .flatMap(existingAttendance -> {
                                existingAttendance.setIpAddress(requestAttendance.getIpAddress());
                                existingAttendance.setDeviceId(requestAttendance.getDeviceId());
                                existingAttendance.setQrCodeId(requestQrGenerator.getQrCodeId());
                                existingAttendance.presented();
                                existingAttendance.validateLocation();

                                return attendanceRepository.save(existingAttendance)
                                        .retryWhen(retryConfig.createRetrySpec("update-attendance"))
                                        .map(saved -> true);
                            });
                });
    }

    public Mono<Boolean> developPresence(UUID lectureId,UUID studentId,UUID qrCodeId ){
        return findLatestAttendanceForStudentInLecture(studentId,lectureId)
                .flatMap(attendance -> {
                    attendance.setDeviceId("ALL45HJ465");
                    attendance.setQrCodeId(qrCodeId);
                    attendance.setLocationVerified(true);
                    attendance.setIpAddress("192.168.1.10");
                    attendance.setPresent(true);
                    attendance.setCheckInTime(Instant.now());
                    return attendanceRepository.save(attendance)
                            .retryWhen(retryConfig.createRetrySpec("update-attendance"))
                            .map(saved -> true);
                });
    }

    // ===== Find By Lecture =====
    public Flux<Attendance> findByLectureId(UUID lectureId) {
        log.debug("Finding attendance records for lecture ID: {}", lectureId);
        return attendanceRepository.findByLectureId(lectureId);
    }

    // ===== Find By Student =====
    public Flux<Attendance> findByStudentId(UUID studentAcademicMemberId) {
        log.debug("Finding attendance records for student ID: {}", studentAcademicMemberId);
        return attendanceRepository.findByStudentAcademicMemberId(studentAcademicMemberId);
    }

    // ===== Find By QR Code =====
    public Flux<Attendance> findByQrCodeId(UUID qrCodeId) {
        log.debug("Finding attendance records for QR code ID: {}", qrCodeId);
        return attendanceRepository.findByQrCodeId(qrCodeId);
    }

    // ===== Find Present Attendance =====
    public Flux<Attendance> findPresentInLecture(UUID lectureId) {
        log.debug("Finding present attendees for lecture ID: {}", lectureId);
        return attendanceRepository.findByLectureIdAndIsPresent(lectureId, true);
    }

    // ===== Find Absent Attendance =====
    public Flux<Attendance> findAbsentInLecture(UUID lectureId) {
        log.debug("Finding absent attendees for lecture ID: {}", lectureId);
        return attendanceRepository.findByLectureIdAndIsPresent(lectureId, false);
    }

    // ===== Find Attendance by Lecture and Student =====
    public Mono<Attendance> findByLectureAndStudent(UUID lectureId, UUID studentAcademicMemberId) {
        return attendanceRepository.findByLectureIdAndStudentAcademicMemberId(lectureId, studentAcademicMemberId)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("No attendance found for lecture: {}, student: {}", lectureId, studentAcademicMemberId)
                ));
    }

    // ===== Count Present in Lecture =====
    public Mono<Long> countPresentInLecture(UUID lectureId) {
        log.debug("Counting present attendees for lecture ID: {}", lectureId);
        return attendanceRepository.countPresentInLecture(lectureId);
    }

    // ===== Count Absent in Lecture =====
    public Mono<Long> countAbsentInLecture(UUID lectureId) {
        log.debug("Counting absent attendees for lecture ID: {}", lectureId);
        return attendanceRepository.countAbsentInLecture(lectureId);
    }

    // ===== Find Attendance by Time Range =====
    public Flux<Attendance> findByTimeRange(UUID lectureId, Instant startTime, Instant endTime) {
        log.debug("Finding attendance for lecture: {} between {} and {}", lectureId, startTime, endTime);
        return attendanceRepository.findByLectureIdAndTimeRange(lectureId, startTime, endTime);
    }

    // ===== Find by Device ID =====
    public Flux<Attendance> findByDeviceId(String deviceId) {
        log.debug("Finding attendance records for device ID: {}", deviceId);
        return attendanceRepository.findByDeviceId(deviceId);
    }

    // ===== Find Latest Attendance for Student in Lecture =====
    public Mono<Attendance> findLatestAttendanceForStudentInLecture(UUID studentId, UUID lectureId) {
        log.debug("Finding latest attendance for student: {} in lecture: {}", studentId, lectureId);
        return attendanceRepository.findLatestAttendanceForStudentInLecture(studentId, lectureId)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("No attendance history found for student: {} in lecture: {}", studentId, lectureId)
                ));
    }

    // ===== Find Student Attendance History =====
    public Flux<Attendance> findStudentAttendanceHistory(UUID studentId, int limit) {
        log.debug("Finding attendance history for student: {} (limit: {})", studentId, limit);
        return attendanceRepository.findStudentAttendanceHistory(studentId, limit);
    }

    // ===== Check if Student Already Checked In =====
    public Mono<Boolean> hasCheckedIn(UUID studentId, UUID lectureId) {
        log.debug("Checking if student: {} checked in lecture: {}", studentId, lectureId);
        return attendanceRepository.existsByStudentAndLecture(studentId, lectureId);
    }

    // ===== Get Attendance Statistics =====
    public Mono<AttendanceStatisticsResponse> getAttendanceStatistics(UUID lectureId) {
        log.info("Fetching attendance statistics for lecture ID: {}", lectureId);
        return attendanceRepository.getAttendanceStatistics(lectureId)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("No statistics found for lecture ID: {}", lectureId)
                ));
    }

    // ===== Update Attendance =====
    public Mono<Attendance> update(Attendance attendance) {
        log.info("Attempting to update Attendance ID: {}", attendance.getAttendanceId());
        return attendanceRepository.save(attendance)
                .retryWhen(retryConfig.createRetrySpec("Update Attendance"))
                .doOnSuccess(updated -> log.info("Updated Attendance ID: {}", updated.getAttendanceId()));
    }

    // ===== Mark as Present =====
    public Mono<Attendance> markAsPresent(UUID studentId, UUID lectureId) {
        log.info("Marking attendance {} as present", studentId);
        return findLatestAttendanceForStudentInLecture(studentId,lectureId)
                .flatMap(attendance -> {
                    attendance.presented().validateLocation();
                    return update(attendance);
                });
    }

    // ===== Mark as Absent =====
    public Mono<Attendance> markAsAbsent(UUID studentId, UUID lectureId) {
        log.info("Marking attendance {} as absent", studentId);
        return findLatestAttendanceForStudentInLecture(studentId,lectureId)
                .flatMap(attendance -> {
                    attendance.absent().validateLocation();
                    return update(attendance);
                });
    }
}