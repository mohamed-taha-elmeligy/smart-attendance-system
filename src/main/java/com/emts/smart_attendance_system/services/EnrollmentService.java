package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.Enrollment;
import com.emts.smart_attendance_system.repositories.EnrollmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: EnrollmentService.java
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
public class EnrollmentService {
    private EnrollmentRepository enrollmentRepository;
    private RetryConfig retryConfig;

    // ===== Create =====
    public Mono<Enrollment> addOne(Enrollment enrollment) {
        log.info("Attempting to save Enrollment for course: {} and student: {}",
                enrollment.getCourseId(), enrollment.getStudentAcademicMember());
        return enrollmentRepository.save(enrollment)
                .retryWhen(retryConfig.createRetrySpec("Add Enrollment"))
                .doOnSuccess(saved -> log.info("Saved Enrollment ID: {}", saved.getEnrollmentId()));
    }

    // ===== Update =====
    public Mono<Enrollment> update(Enrollment enrollment) {
        log.info("Attempting to update Enrollment: {}", enrollment.getEnrollmentId());
        return enrollmentRepository.save(enrollment)
                .retryWhen(retryConfig.createRetrySpec("Update Enrollment"))
                .doOnSuccess(updated -> log.info("Updated Enrollment ID: {}", updated.getEnrollmentId()));
    }

    // ===== Find Active Enrollments =====
    public Flux<Enrollment> findAllActive() {
        log.debug("Fetching all active enrollments");
        return enrollmentRepository.findAllActive();
    }

    public Mono<Enrollment> findByIdActive(UUID enrollmentId) {
        log.debug("Fetching active enrollment by ID: {}", enrollmentId);
        return enrollmentRepository.findByIdActive(enrollmentId);
    }

    // ===== Find by Course =====
    public Flux<Enrollment> findByCourseId(UUID courseId) {
        log.debug("Fetching active enrollments for course: {}", courseId);
        return enrollmentRepository.findByCourseId(courseId);
    }

    // ===== Find by Student =====
    public Flux<Enrollment> findByStudentAcademicMember(UUID studentAcademicMember) {
        log.debug("Fetching active enrollments for student: {}", studentAcademicMember);
        return enrollmentRepository.findByStudentAcademicMember(studentAcademicMember);
    }

    // ===== Find Specific Enrollment =====
    public Mono<Enrollment> findByExistsActive(UUID courseId, UUID studentAcademicMember) {
        log.debug("Fetching active enrollment for course: {} and student: {}", courseId, studentAcademicMember);
        return enrollmentRepository.findByExistsActive(courseId, studentAcademicMember);
    }

    public Mono<Enrollment> findByExists(UUID courseId, UUID studentAcademicMember) {
        log.debug("Fetching enrollment for course: {} and student: {} (including deleted)", courseId, studentAcademicMember);
        return enrollmentRepository.findByExists(courseId, studentAcademicMember);
    }

    // ===== Count Operations =====
    public Mono<Long> countByCourseId(UUID courseId) {
        log.debug("Counting active enrollments for course: {}", courseId);
        return enrollmentRepository.countByCourseId(courseId)
                .doOnNext(count -> log.debug("Total active enrollments for course {}: {}", courseId, count));
    }

    public Mono<Long> countByStudentAcademicMember(UUID studentAcademicMember) {
        log.debug("Counting active enrollments for student: {}", studentAcademicMember);
        return enrollmentRepository.countByStudentAcademicMember(studentAcademicMember)
                .doOnNext(count -> log.debug("Total active enrollments for student {}: {}", studentAcademicMember, count));
    }

    public Mono<Long> countAllActive() {
        log.debug("Counting all active enrollments");
        return enrollmentRepository.countAllActive()
                .doOnNext(count -> log.debug("Total active enrollments: {}", count));
    }

    // ===== Soft Delete Operations =====
    public Mono<Integer> softDeleteByCourseId(UUID courseId) {
        log.info("Soft deleting all enrollments for course: {}", courseId);
        return enrollmentRepository.softDeleteByCourseId(courseId)
                .retryWhen(retryConfig.createRetrySpec("Soft Delete Enrollment by Course"))
                .doOnSuccess(count -> log.info("Soft deleted {} enrollments for course: {}", count, courseId));
    }

    public Mono<Integer> softDeleteByStudentAcademicMember(UUID studentAcademicMember) {
        log.info("Soft deleting all enrollments for student: {}", studentAcademicMember);
        return enrollmentRepository.softDeleteByStudentAcademicMember(studentAcademicMember)
                .retryWhen(retryConfig.createRetrySpec("Soft Delete Enrollment by Student"))
                .doOnSuccess(count -> log.info("Soft deleted {} enrollments for student: {}", count, studentAcademicMember));
    }

    public Mono<Integer> softDeleteById(UUID enrollmentId) {
        log.info("Soft deleting enrollment: {}", enrollmentId);
        return findByIdActive(enrollmentId)
                .flatMap(enrollment -> {
                    enrollment.delete();
                    return enrollmentRepository.save(enrollment);
                })
                .retryWhen(retryConfig.createRetrySpec("Soft Delete Enrollment"))
                .then(Mono.just(1))
                .doOnSuccess(count -> log.info("Soft deleted enrollment: {}", enrollmentId));
    }

    // ===== Restore Operations =====
    public Mono<Integer> restoreEnrollment(UUID enrollmentId) {
        log.info("Restoring enrollment: {}", enrollmentId);
        return enrollmentRepository.restoreEnrollment(enrollmentId)
                .retryWhen(retryConfig.createRetrySpec("Restore Enrollment"))
                .doOnSuccess(count -> log.info("Restored {} enrollment(s): {}", count, enrollmentId));
    }

    public Mono<Integer> restoreByCourseId(UUID courseId) {
        log.info("Restoring all deleted enrollments for course: {}", courseId);
        return enrollmentRepository.restoreByCourseId(courseId)
                .retryWhen(retryConfig.createRetrySpec("Restore Enrollment by Course"))
                .doOnSuccess(count -> log.info("Restored {} enrollments for course: {}", count, courseId));
    }

    // ===== Find Deleted Enrollments =====
    public Flux<Enrollment> findAllDeleted() {
        log.debug("Fetching all deleted enrollments");
        return enrollmentRepository.findAllDeleted();
    }

    public Flux<Enrollment> findDeletedByCourseId(UUID courseId) {
        log.debug("Fetching deleted enrollments for course: {}", courseId);
        return enrollmentRepository.findDeletedByCourseId(courseId);
    }
}

