package com.emts.smart_attendance_system.processors;

import com.emts.smart_attendance_system.entities.Attendance;
import com.emts.smart_attendance_system.entities.Enrollment;
import com.emts.smart_attendance_system.entities.Lecture;
import com.emts.smart_attendance_system.services.AttendanceService;
import com.emts.smart_attendance_system.services.EnrollmentService;
import com.emts.smart_attendance_system.services.LectureService;
import com.emts.smart_attendance_system.utils.AttendanceProcessResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * *******************************************************************
 * File: AttendanceProcessor.java
 * Package: com.emts.smart_attendance_system.processors
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 10/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@Slf4j
@AllArgsConstructor
public class AttendanceProcessor {

    private final AttendanceService attendanceService;
    private final EnrollmentService enrollmentService;
    private final LectureService lectureService;

    @Scheduled(cron = "0 0 6 * * *")
    public void scheduleAttendanceCreation() {
        log.info("Start creating attendance records for daily lectures");
//        processAttendances(createAttendance());
        attendanceService.addAttendances(createAttendance());
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void retryFailedAttendances() {
        log.info("Retry failed attendance records");
        processAttendances(createAttendance());
    }

    private void processAttendances(Flux<Attendance> attendances) {
        attendances
                .collectList()
                .flatMap(attendanceList -> {
                    if (attendanceList.isEmpty()) {
                        log.warn("No lectures or records to save");
                        return Mono.just(new AttendanceProcessResult(0, 0));
                    }

                    log.info("Saving {} attendance record", attendanceList.size());
                    return attendanceService.addAttendances(
                                    Flux.fromIterable(attendanceList))
                            .collectList()
                            .map(savedList -> {
                                int successful = savedList.size();
                                int failed = attendanceList.size() - successful;

                                if (failed > 0) {
                                    logFailedAttendances(attendanceList, savedList);
                                }

                                return new AttendanceProcessResult(successful, failed);
                            })
                            .onErrorResume(error -> {
                                log.error("Critical save error: {}",                                        error.getMessage(), error);
                                return Mono.just(
                                        new AttendanceProcessResult(
                                                0,
                                                attendanceList.size()
                                        )
                                );
                            });
                })
                .doOnSuccess(result -> {
                    if (result.isFullySuccessful()) {
                        log.info("Successfully saving all records ({} record)",                                result.getSuccessful());
                    } else {
                        log.warn(
                                "Result of memorization - Pass: {}, Fail: {}",                                result.getSuccessful(),
                                result.getFailed()
                        );
                    }
                })
                .doOnError(error ->
                        log.error("Error in record handling: {}", error.getMessage())                )
                .subscribe(
                        result -> {}, // onNext
                        error -> log.error("Error handling failed", error), // onError
                        () -> log.debug("Log processing completed") // onComplete
                );
    }

    private void logFailedAttendances(
            List<Attendance> original,
            List<Attendance> saved) {

        Set<UUID> savedIds = saved.stream()
                .map(Attendance::getAttendanceId)
                .collect(Collectors.toSet());

        original.stream()
                .filter(att -> !savedIds.contains(att.getAttendanceId()))
                .forEach(failed ->
                        log.warn(
                                "Save failed: Lecture={}, Student={}",
                                failed.getLectureId(),
                                failed.getStudentAcademicMemberId()
                        )
                );
    }

    private Flux<Attendance> createAttendance() {
        return getLectureInDay()
                .flatMap(lecture ->
                        getEnrollment(lecture.getCourseId())
                                .map(enrollment ->
                                        buildAttendance(lecture, enrollment)
                                )
                )
                .doOnError(error ->
                        log.error("Error creating records: {}", error.getMessage())
                );
    }

    private Attendance buildAttendance(Lecture lecture, Enrollment enrollment) {
        Attendance attendance = new Attendance();
        attendance.setStudentAcademicMemberId(enrollment.getStudentAcademicMember());
        attendance.setLectureId(lecture.getLectureId());
        return attendance;
    }

    private Flux<Lecture> getLectureInDay() {
        return lectureService.findByLectureDate(LocalDate.now())
                .doOnError(error ->
                        log.error("Error fetching lectures: {}", error.getMessage())
                );
    }

    private Flux<Enrollment> getEnrollment(UUID courseId) {
        return enrollmentService.findByCourseId(courseId)
                .doOnError(error ->
                        log.error("Error fetching course recordings {}: {}",
                                courseId, error.getMessage())
                );
    }
}

