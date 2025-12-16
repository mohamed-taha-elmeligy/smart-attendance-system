package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.Lecture;
import com.emts.smart_attendance_system.repositories.LectureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 21/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
@AllArgsConstructor
@Slf4j
public class LectureService {
    private LectureRepository lectureRepository;
    private RetryConfig retryConfig;

    // ===== Create =====
    public Mono<Lecture> addOne(Lecture lecture) {
        log.info("Attempting to save Lecture for course: {}", lecture.getCourseId());

        return findByCourseIdAndLectureDate(lecture.getCourseId(), lecture.getLectureDate())
                .hasElements()
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        log.warn("Lecture for course '{}' on '{}' by instructor '{}' already exists. Skipping save.",
                                lecture.getCourseId(), lecture.getLectureDate(), lecture.getInstructorAcademicMemberId());
                        return Mono.empty();
                    } else {
                        return lectureRepository.save(lecture)
                                .retryWhen(retryConfig.createRetrySpec("Add Lecture"))
                                .doOnSuccess(saved -> log.info("Saved Lecture ID: {}", saved.getLectureId()));
                    }
                });
    }


    // ===== Update =====
    public Mono<Lecture> update(Lecture lecture) {
        log.info("Attempting to update Lecture: {}", lecture.getLectureId());
        return lectureRepository.save(lecture)
                .retryWhen(retryConfig.createRetrySpec("Update Lecture"))
                .doOnSuccess(updated -> log.info("Updated Lecture ID: {}", updated.getLectureId()));
    }

    // ===== Find by Course =====
    public Flux<Lecture> findByCourseId(UUID courseId) {
        log.debug("Fetching lectures for course: {}", courseId);
        return lectureRepository.findByCourseIdAndSoftDeleteFalse(courseId);
    }

    public Flux<Lecture> findActiveByCourseId(UUID courseId) {
        log.debug("Fetching active lectures for course: {}", courseId);
        return lectureRepository.findByCourseIdAndStatusTrueAndSoftDeleteFalse(courseId);
    }

    // ===== Find by Instructor =====
    public Flux<Lecture> findByInstructorId(UUID instructorAcademicMemberId) {
        log.debug("Fetching lectures for instructor: {}", instructorAcademicMemberId);
        return lectureRepository.findByInstructorAcademicMemberIdAndSoftDeleteFalse(instructorAcademicMemberId);
    }

    public Flux<Lecture> findActiveByInstructorId(UUID instructorAcademicMemberId) {
        log.debug("Fetching active lectures for instructor: {}", instructorAcademicMemberId);
        return lectureRepository.findByInstructorAcademicMemberIdAndStatusTrueAndSoftDeleteFalse(instructorAcademicMemberId);
    }

    // ===== Find by Course and Instructor =====
    public Flux<Lecture> findByCourseIdAndInstructorId(UUID courseId, UUID instructorAcademicMemberId) {
        log.debug("Fetching lectures for course: {} and instructor: {}", courseId, instructorAcademicMemberId);
        return lectureRepository.findByCourseIdAndInstructorAcademicMemberIdAndSoftDeleteFalse(courseId, instructorAcademicMemberId);
    }

    public Flux<Lecture> findActiveByCourseIdAndInstructorId(UUID courseId, UUID instructorAcademicMemberId) {
        log.debug("Fetching active lectures for course: {} and instructor: {}", courseId, instructorAcademicMemberId);
        return lectureRepository.findByCourseIdAndInstructorAcademicMemberIdAndStatusTrueAndSoftDeleteFalse(courseId, instructorAcademicMemberId);
    }

    // ===== Find by Date =====
    public Flux<Lecture> findByLectureDate(LocalDate lectureDate) {
        log.debug("Fetching lectures for date: {}", lectureDate);
        return lectureRepository.findByLectureDateAndSoftDeleteFalse(lectureDate);
    }

    public Flux<Lecture> findActiveByLectureDate(LocalDate lectureDate) {
        log.debug("Fetching active lectures for date: {}", lectureDate);
        return lectureRepository.findByLectureDateAndStatusTrueAndSoftDeleteFalse(lectureDate);
    }

    public Flux<Lecture> findByLectureDateBetween(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching lectures between: {} and {}", startDate, endDate);
        return lectureRepository.findByLectureDateBetweenAndSoftDeleteFalse(startDate, endDate);
    }

    public Flux<Lecture> findActiveByLectureDateBetween(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching active lectures between: {} and {}", startDate, endDate);
        return lectureRepository.findByLectureDateBetweenAndStatusTrueAndSoftDeleteFalse(startDate, endDate);
    }

    // ===== Find by Instructor and Date =====
    public Flux<Lecture> findByInstructorIdAndLectureDate(UUID instructorAcademicMemberId, LocalDate lectureDate) {
        log.debug("Fetching lectures for instructor: {} on date: {}", instructorAcademicMemberId, lectureDate);
        return lectureRepository.findByInstructorAcademicMemberIdAndLectureDateAndSoftDeleteFalse(instructorAcademicMemberId, lectureDate);
    }

    public Flux<Lecture> findByInstructorIdAndLectureDateBetween(UUID instructorAcademicMemberId, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching lectures for instructor: {} between: {} and {}", instructorAcademicMemberId, startDate, endDate);
        return lectureRepository.findByInstructorAcademicMemberIdAndLectureDateBetweenAndSoftDeleteFalse(instructorAcademicMemberId, startDate, endDate);
    }

    // ===== Find by Course and Date =====
    public Flux<Lecture> findByCourseIdAndLectureDate(UUID courseId, LocalDate lectureDate) {
        log.debug("Fetching lectures for course: {} on date: {}", courseId, lectureDate);
        return lectureRepository.findByCourseIdAndLectureDateAndSoftDeleteFalse(courseId, lectureDate);
    }

    public Flux<Lecture> findByCourseIdAndLectureDateBetween(UUID courseId, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching lectures for course: {} between: {} and {}", courseId, startDate, endDate);
        return lectureRepository.findByCourseIdAndLectureDateBetweenAndSoftDeleteFalse(courseId, startDate, endDate);
    }

    // ===== Find by Room =====
    public Flux<Lecture> findByRoom(String room) {
        log.debug("Fetching lectures for room: {}", room);
        return lectureRepository.findByRoomAndSoftDeleteFalse(room);
    }

    public Flux<Lecture> findActiveByRoom(String room) {
        log.debug("Fetching active lectures for room: {}", room);
        return lectureRepository.findByRoomAndStatusTrueAndSoftDeleteFalse(room);
    }

    // ===== Status Operations =====
    public Mono<Lecture> activate(UUID lectureId) {
        log.info("Activating lecture: {}", lectureId);
        return lectureRepository.activate(lectureId)
                .retryWhen(retryConfig.createRetrySpec("Activate Lecture"))
                .doOnSuccess(activated -> log.info("Lecture activated: {}", lectureId));
    }

    public Mono<Lecture> deactivate(UUID lectureId) {
        log.info("Deactivating lecture: {}", lectureId);
        return lectureRepository.deactivate(lectureId)
                .retryWhen(retryConfig.createRetrySpec("Deactivate Lecture"))
                .doOnSuccess(deactivated -> log.info("Lecture deactivated: {}", lectureId));
    }

    // ===== Soft Delete Operations =====
    public Mono<Lecture> softDelete(UUID lectureId) {
        log.info("Soft deleting lecture: {}", lectureId);
        return lectureRepository.softDelete(lectureId)
                .retryWhen(retryConfig.createRetrySpec("Soft Delete Lecture"))
                .doOnSuccess(deleted -> log.info("Lecture soft deleted: {}", lectureId));
    }

    public Mono<Lecture> restore(UUID lectureId) {
        log.info("Restoring lecture: {}", lectureId);
        return lectureRepository.restore(lectureId)
                .retryWhen(retryConfig.createRetrySpec("Restore Lecture"))
                .doOnSuccess(restored -> log.info("Lecture restored: {}", lectureId));
    }

    // ===== Find Active/Inactive/Deleted =====
    public Flux<Lecture> findActive() {
        log.debug("Fetching all active lectures");
        return lectureRepository.findActive();
    }

    public Flux<Lecture> findInactive() {
        log.debug("Fetching all inactive lectures");
        return lectureRepository.findInactive();
    }

    public Flux<Lecture> findDeleted() {
        log.debug("Fetching all deleted lectures");
        return lectureRepository.findDeleted();
    }
    public Flux<Lecture>findByDayOfWeek(DayOfWeek dayOfWeek) {
        log.debug("Fetching lectures by DayOfWeek: {}",dayOfWeek);
        return lectureRepository.findByDayOfWeek(dayOfWeek);
    }

    // ===== Count =====
    public Mono<Long> countByCourseId(UUID courseId) {
        return lectureRepository.countByCourseIdAndSoftDeleteFalse(courseId)
                .doOnNext(count -> log.debug("Total lectures for course {}: {}", courseId, count));
    }

    public Mono<Long> countByInstructorId(UUID instructorAcademicMemberId) {
        return lectureRepository.countByInstructorAcademicMemberIdAndSoftDeleteFalse(instructorAcademicMemberId)
                .doOnNext(count -> log.debug("Total lectures for instructor {}: {}", instructorAcademicMemberId, count));
    }
}
