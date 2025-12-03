package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.EnrollmentConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestEnrollment;
import com.emts.smart_attendance_system.dtos.responses.ResponseEnrollment;
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
 * File: EnrollmentController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@RestController
@RequestMapping("/api/v1/enrollment")
@AllArgsConstructor
@Slf4j
public class EnrollmentController {
    private EnrollmentConverter enrollmentConverter;

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseEnrollment>> addOne(
            @Valid @RequestBody RequestEnrollment requestEnrollment) {
        log.debug("Adding new enrollment for course: {} and student: {}",
                requestEnrollment.getCourseId(), requestEnrollment.getStudentAcademicMember());
        return enrollmentConverter.addOne(requestEnrollment)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error adding enrollment: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseEnrollment>> update(
            @RequestParam @NonNull UUID enrollmentId,
            @Valid @RequestBody RequestEnrollment requestEnrollment) {
        log.debug("Updating enrollment: {}", enrollmentId);
        return enrollmentConverter.update(enrollmentId, requestEnrollment)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error updating enrollment: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/find-all-active")
    public Flux<ResponseEnrollment> findAllActive() {
        log.debug("Fetching all active enrollments");
        return enrollmentConverter.findAllActive()
                .onErrorResume(throwable -> {
                    log.error("Error fetching all active enrollments: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-id")
    public Mono<ResponseEntity<ResponseEnrollment>> findByIdActive(
            @RequestParam @NonNull UUID enrollmentId) {
        log.debug("Fetching active enrollment by ID: {}", enrollmentId);
        return enrollmentConverter.findByIdActive(enrollmentId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> {
                    log.error("Error fetching enrollment by ID: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/find-by-course")
    public Flux<ResponseEnrollment> findByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Fetching active enrollments for course: {}", courseId);
        return enrollmentConverter.findByCourseId(courseId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching enrollments by course: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-student")
    public Flux<ResponseEnrollment> findByStudentAcademicMember(
            @RequestParam @NonNull UUID studentAcademicMember) {
        log.debug("Fetching active enrollments for student: {}", studentAcademicMember);
        return enrollmentConverter.findByStudentAcademicMember(studentAcademicMember)
                .onErrorResume(throwable -> {
                    log.error("Error fetching enrollments by student: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-by-course-and-student")
    public Mono<ResponseEntity<ResponseEnrollment>> findByExistsActive(
            @RequestParam @NonNull UUID courseId,
            @RequestParam @NonNull UUID studentAcademicMember) {
        log.debug("Fetching active enrollment for course: {} and student: {}", courseId, studentAcademicMember);
        return enrollmentConverter.findByExistsActive(courseId, studentAcademicMember)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> {
                    log.error("Error fetching enrollment by course and student: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/check-enrollment")
    public Mono<ResponseEntity<ResponseEnrollment>> findByExists(
            @RequestParam @NonNull UUID courseId,
            @RequestParam @NonNull UUID studentAcademicMember) {
        log.debug("Checking enrollment for course: {} and student: {} (including deleted)", courseId, studentAcademicMember);
        return enrollmentConverter.findByExists(courseId, studentAcademicMember)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(throwable -> {
                    log.error("Error checking enrollment: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping("/count-by-course")
    public Mono<ResponseEntity<Long>> countByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Counting active enrollments for course: {}", courseId);
        return enrollmentConverter.countByCourseId(courseId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting enrollments by course: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/count-by-student")
    public Mono<ResponseEntity<Long>> countByStudentAcademicMember(
            @RequestParam @NonNull UUID studentAcademicMember) {
        log.debug("Counting active enrollments for student: {}", studentAcademicMember);
        return enrollmentConverter.countByStudentAcademicMember(studentAcademicMember)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting enrollments by student: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/count-all")
    public Mono<ResponseEntity<Long>> countAllActive() {
        log.debug("Counting all active enrollments");
        return enrollmentConverter.countAllActive()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting all active enrollments: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @DeleteMapping("/delete-by-id")
    public Mono<ResponseEntity<Integer>> softDeleteById(
            @RequestParam @NonNull UUID enrollmentId) {
        log.debug("Soft deleting enrollment: {}", enrollmentId);
        return enrollmentConverter.softDeleteById(enrollmentId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error soft deleting enrollment: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @DeleteMapping("/delete-by-course")
    public Mono<ResponseEntity<Integer>> softDeleteByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Soft deleting all enrollments for course: {}", courseId);
        return enrollmentConverter.softDeleteByCourseId(courseId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error soft deleting enrollments by course: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @DeleteMapping("/delete-by-student")
    public Mono<ResponseEntity<Integer>> softDeleteByStudentAcademicMember(
            @RequestParam @NonNull UUID studentAcademicMember) {
        log.debug("Soft deleting all enrollments for student: {}", studentAcademicMember);
        return enrollmentConverter.softDeleteByStudentAcademicMember(studentAcademicMember)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error soft deleting enrollments by student: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/restore")
    public Mono<ResponseEntity<Integer>> restoreEnrollment(
            @RequestParam @NonNull UUID enrollmentId) {
        log.debug("Restoring enrollment: {}", enrollmentId);
        return enrollmentConverter.restoreEnrollment(enrollmentId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error restoring enrollment: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/restore-by-course")
    public Mono<ResponseEntity<Integer>> restoreByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Restoring all deleted enrollments for course: {}", courseId);
        return enrollmentConverter.restoreByCourseId(courseId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error restoring enrollments by course: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/find-all-deleted")
    public Flux<ResponseEnrollment> findAllDeleted() {
        log.debug("Fetching all deleted enrollments");
        return enrollmentConverter.findAllDeleted()
                .onErrorResume(throwable -> {
                    log.error("Error fetching all deleted enrollments: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-deleted-by-course")
    public Flux<ResponseEnrollment> findDeletedByCourseId(
            @RequestParam @NonNull UUID courseId) {
        log.debug("Fetching deleted enrollments for course: {}", courseId);
        return enrollmentConverter.findDeletedByCourseId(courseId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching deleted enrollments by course: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }
}
