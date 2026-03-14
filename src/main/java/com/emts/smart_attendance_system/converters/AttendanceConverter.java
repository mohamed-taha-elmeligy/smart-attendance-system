package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.AttendanceMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestAttendance;
import com.emts.smart_attendance_system.dtos.requests.RequestQrGenerator;
import com.emts.smart_attendance_system.dtos.responses.ResponseAttendance;
import com.emts.smart_attendance_system.dtos.responses.AttendanceStatisticsResponse;
import com.emts.smart_attendance_system.services.AttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AttendanceConverter.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 19/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@AllArgsConstructor
public class AttendanceConverter {
    private AttendanceService attendanceService;
    private AttendanceMapper attendanceMapper;


    public Mono<Boolean> present (RequestAttendance requestAttendance){
        return attendanceService.presence(requestAttendance);
    }

    public Mono<Boolean> developPresence(UUID lectureId,UUID studentId,UUID qrCodeId ){
        return attendanceService.developPresence(lectureId,studentId,qrCodeId);
    }

    public Flux<ResponseAttendance> findByLectureId(UUID lectureId) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findByLectureId(lectureId)
        );
    }

    public Flux<ResponseAttendance> findByStudentId(UUID studentAcademicMemberId) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findByStudentId(studentAcademicMemberId)
        );
    }

    public Flux<ResponseAttendance> findByQrCodeId(UUID qrCodeId) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findByQrCodeId(qrCodeId)
        );
    }

    // ===== Filter Methods =====

    public Flux<ResponseAttendance> findPresentInLecture(UUID lectureId) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findPresentInLecture(lectureId)
        );
    }

    public Flux<ResponseAttendance> findAbsentInLecture(UUID lectureId) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findAbsentInLecture(lectureId)
        );
    }

    public Mono<ResponseAttendance> findByLectureAndStudent(UUID lectureId, UUID studentAcademicMemberId) {
        return attendanceMapper.toResponseMono(
                attendanceService.findByLectureAndStudent(lectureId, studentAcademicMemberId)
        );
    }

    // ===== Count Methods =====

    public Mono<Long> countPresentInLecture(UUID lectureId) {
        return attendanceService.countPresentInLecture(lectureId);
    }

    public Mono<Long> countAbsentInLecture(UUID lectureId) {
        return attendanceService.countAbsentInLecture(lectureId);
    }

    // ===== Time Range Methods =====

    public Flux<ResponseAttendance> findByTimeRange(UUID lectureId, Instant startTime, Instant endTime) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findByTimeRange(lectureId, startTime, endTime)
        );
    }

    // ===== Device Methods =====

    public Flux<ResponseAttendance> findByDeviceId(String deviceId) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findByDeviceId(deviceId)
        );
    }

    // ===== Student History Methods =====

    public Mono<ResponseAttendance> findLatestAttendanceForStudentInLecture(UUID studentId, UUID lectureId) {
        return attendanceMapper.toResponseMono(
                attendanceService.findLatestAttendanceForStudentInLecture(studentId, lectureId)
        );
    }

    public Flux<ResponseAttendance> findStudentAttendanceHistory(UUID studentId, int limit) {
        return attendanceMapper.toResponseFlux(
                attendanceService.findStudentAttendanceHistory(studentId, limit)
        );
    }

    // ===== Check-in Methods =====

    public Mono<Boolean> hasCheckedIn(UUID studentId, UUID lectureId) {
        return attendanceService.hasCheckedIn(studentId, lectureId);
    }

    // ===== Mark Present/Absent Methods =====

    public Mono<ResponseAttendance> markAsPresent(UUID studentId, UUID lectureId) {
        return attendanceMapper.toResponseMono(
                attendanceService.markAsPresent(studentId,lectureId)
        );
    }

    public Mono<ResponseAttendance> markAsAbsent(UUID studentId, UUID lectureId) {
        return attendanceMapper.toResponseMono(
                attendanceService.markAsAbsent(studentId,lectureId)
        );
    }

    // ===== Statistics Methods =====
    public Mono<AttendanceStatisticsResponse> getAttendanceStatistics(UUID lectureId) {
        return attendanceService.getAttendanceStatistics(lectureId);
    }

}