package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.LectureConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestLecture;
import com.emts.smart_attendance_system.dtos.responses.ResponseLecture;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

/**
 * *******************************************************************
 * File: LectureController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 20/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@RestController
@RequestMapping("/api/v1/lecture")
@AllArgsConstructor
@Slf4j
public class LectureController {
    private LectureConverter lectureConverter;

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseLecture>> addOne(
            @Valid @RequestBody RequestLecture requestLecture) {
        log.debug("Adding new lecture for course: {}", requestLecture.getCourseId());
        return lectureConverter.addOne(requestLecture)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error adding lecture: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseLecture>> update(
            @RequestParam @NonNull UUID lectureId,
            @Valid @RequestBody RequestLecture requestLecture) {
        log.debug("Updating lecture: {}", lectureId);
        return lectureConverter.update(lectureId, requestLecture)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error updating lecture: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/find-by-course")
    public Flux<ResponseLecture> findByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Fetching lectures for course: {}", courseId);
        return lectureConverter.findByCourseId(courseId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by course: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-active-by-course")
    public Flux<ResponseLecture> findActiveByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Fetching active lectures for course: {}", courseId);
        return lectureConverter.findActiveByCourseId(courseId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching active lectures by course: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-instructor")
    public Flux<ResponseLecture> findByInstructorId(
            @RequestParam @NonNull UUID instructorAcademicMemberId) {
        log.debug("Fetching lectures for instructor: {}", instructorAcademicMemberId);
        return lectureConverter.findByInstructorId(instructorAcademicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by instructor: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-active-by-instructor")
    public Flux<ResponseLecture> findActiveByInstructorId(
            @RequestParam @NonNull UUID instructorAcademicMemberId) {
        log.debug("Fetching active lectures for instructor: {}", instructorAcademicMemberId);
        return lectureConverter.findActiveByInstructorId(instructorAcademicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching active lectures by instructor: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-course-and-instructor")
    public Flux<ResponseLecture> findByCourseIdAndInstructorId(
            @RequestParam @NonNull UUID courseId,
            @RequestParam @NonNull UUID instructorAcademicMemberId) {
        log.debug("Fetching lectures for course: {} and instructor: {}", courseId, instructorAcademicMemberId);
        return lectureConverter.findByCourseIdAndInstructorId(courseId, instructorAcademicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by course and instructor: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-active-by-course-and-instructor")
    public Flux<ResponseLecture> findActiveByCourseIdAndInstructorId(
            @RequestParam @NonNull UUID courseId,
            @RequestParam @NonNull UUID instructorAcademicMemberId) {
        log.debug("Fetching active lectures for course: {} and instructor: {}", courseId, instructorAcademicMemberId);
        return lectureConverter.findActiveByCourseIdAndInstructorId(courseId, instructorAcademicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching active lectures by course and instructor: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-date")
    public Flux<ResponseLecture> findByLectureDate(
            @RequestParam @NonNull LocalDate lectureDate) {
        log.debug("Fetching lectures for date: {}", lectureDate);
        return lectureConverter.findByLectureDate(lectureDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by date: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-active-by-date")
    public Flux<ResponseLecture> findActiveByLectureDate(
            @RequestParam @NonNull LocalDate lectureDate) {
        log.debug("Fetching active lectures for date: {}", lectureDate);
        return lectureConverter.findActiveByLectureDate(lectureDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching active lectures by date: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-date-range")
    public Flux<ResponseLecture> findByLectureDateBetween(
            @RequestParam @NonNull LocalDate startDate,
            @RequestParam @NonNull LocalDate endDate) {
        log.debug("Fetching lectures between: {} and {}", startDate, endDate);
        return lectureConverter.findByLectureDateBetween(startDate, endDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by date range: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-active-by-date-range")
    public Flux<ResponseLecture> findActiveByLectureDateBetween(
            @RequestParam @NonNull LocalDate startDate,
            @RequestParam @NonNull LocalDate endDate) {
        log.debug("Fetching active lectures between: {} and {}", startDate, endDate);
        return lectureConverter.findActiveByLectureDateBetween(startDate, endDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching active lectures by date range: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-instructor-and-date")
    public Flux<ResponseLecture> findByInstructorIdAndLectureDate(
            @RequestParam @NonNull UUID instructorAcademicMemberId,
            @RequestParam @NonNull LocalDate lectureDate) {
        log.debug("Fetching lectures for instructor: {} on date: {}", instructorAcademicMemberId, lectureDate);
        return lectureConverter.findByInstructorIdAndLectureDate(instructorAcademicMemberId, lectureDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by instructor and date: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-instructor-and-date-range")
    public Flux<ResponseLecture> findByInstructorIdAndLectureDateBetween(
            @RequestParam @NonNull UUID instructorAcademicMemberId,
            @RequestParam @NonNull LocalDate startDate,
            @RequestParam @NonNull LocalDate endDate) {
        log.debug("Fetching lectures for instructor: {} between: {} and {}", instructorAcademicMemberId, startDate, endDate);
        return lectureConverter.findByInstructorIdAndLectureDateBetween(instructorAcademicMemberId, startDate, endDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by instructor and date range: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-course-and-date")
    public Flux<ResponseLecture> findByCourseIdAndLectureDate(
            @RequestParam @NonNull UUID courseId,
            @RequestParam @NonNull LocalDate lectureDate) {
        log.debug("Fetching lectures for course: {} on date: {}", courseId, lectureDate);
        return lectureConverter.findByCourseIdAndLectureDate(courseId, lectureDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by course and date: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-course-and-date-range")
    public Flux<ResponseLecture> findByCourseIdAndLectureDateBetween(
            @RequestParam @NonNull UUID courseId,
            @RequestParam @NonNull LocalDate startDate,
            @RequestParam @NonNull LocalDate endDate) {
        log.debug("Fetching lectures for course: {} between: {} and {}", courseId, startDate, endDate);
        return lectureConverter.findByCourseIdAndLectureDateBetween(courseId, startDate, endDate)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by course and date range: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-room")
    public Flux<ResponseLecture> findByRoom(
            @RequestParam @NotBlank String room) {
        log.debug("Fetching lectures for room: {}", room);
        return lectureConverter.findByRoom(room)
                .onErrorResume(throwable -> {
                    log.error("Error fetching lectures by room: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-active-by-room")
    public Flux<ResponseLecture> findActiveByRoom(
            @RequestParam @NotBlank String room) {
        log.debug("Fetching active lectures for room: {}", room);
        return lectureConverter.findActiveByRoom(room)
                .onErrorResume(throwable -> {
                    log.error("Error fetching active lectures by room: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @PutMapping("/activate")
    public Mono<ResponseEntity<ResponseLecture>> activate(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Activating lecture: {}", lectureId);
        return lectureConverter.activate(lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error activating lecture: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/deactivate")
    public Mono<ResponseEntity<ResponseLecture>> deactivate(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Deactivating lecture: {}", lectureId);
        return lectureConverter.deactivate(lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error deactivating lecture: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<ResponseLecture>> softDelete(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Soft deleting lecture: {}", lectureId);
        return lectureConverter.softDelete(lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error soft deleting lecture: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/restore")
    public Mono<ResponseEntity<ResponseLecture>> restore(
            @RequestParam @NonNull UUID lectureId) {
        log.debug("Restoring lecture: {}", lectureId);
        return lectureConverter.restore(lectureId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error restoring lecture: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/find-active")
    public Flux<ResponseLecture> findActive() {
        log.debug("Fetching all active lectures");
        return lectureConverter.findActive()
                .onErrorResume(throwable -> {
                    log.error("Error fetching active lectures: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-inactive")
    public Flux<ResponseLecture> findInactive() {
        log.debug("Fetching all inactive lectures");
        return lectureConverter.findInactive()
                .onErrorResume(throwable -> {
                    log.error("Error fetching inactive lectures: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-deleted")
    public Flux<ResponseLecture> findDeleted() {
        log.debug("Fetching all deleted lectures");
        return lectureConverter.findDeleted()
                .onErrorResume(throwable -> {
                    log.error("Error fetching deleted lectures: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/count-by-course")
    public Mono<ResponseEntity<Long>> countByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Counting lectures for course: {}", courseId);
        return lectureConverter.countByCourseId(courseId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting lectures by course: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/count-by-instructor")
    public Mono<ResponseEntity<Long>> countByInstructorId(
            @RequestParam @NonNull UUID instructorAcademicMemberId) {
        log.debug("Counting lectures for instructor: {}", instructorAcademicMemberId);
        return lectureConverter.countByInstructorId(instructorAcademicMemberId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting lectures by instructor: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }
}