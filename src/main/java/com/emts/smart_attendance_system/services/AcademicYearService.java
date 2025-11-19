package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.AcademicYear;
import com.emts.smart_attendance_system.exceptions.exception.CurrentDeleteException;
import com.emts.smart_attendance_system.repositories.AcademicYearRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AcademicYearService.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
@AllArgsConstructor
@Slf4j
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final RetryConfig retryConfig;

    public Mono<AcademicYear> addOne(AcademicYear academicYear) {
        log.info("Attempting to save AcademicYear: {}", academicYear.getCode());
        return academicYearRepository.save(academicYear)
                .retryWhen(retryConfig.createRetrySpec("Add AcademicYear"))
                .doOnSuccess(saved -> log.info("Saved AcademicYear ID: {}", saved.getAcademicYearId()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("AcademicYear '{}' already exists. Skipping save.", academicYear.getCode());
                    return Mono.empty();
                });
    }

    public Mono<AcademicYear> findById(UUID academicYearId) {
        return academicYearRepository.findByAcademicYearIdAndSoftDeleteFalse(academicYearId)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("AcademicYear not found with ID: {}", academicYearId)
                ));
    }

    public Mono<AcademicYear> findByCode(String code) {
        return academicYearRepository.findByCodeAndSoftDeleteFalse(code)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("AcademicYear not found with code: {}", code)
                ));
    }

    public Mono<Boolean> existsByCode(String code) {
        return academicYearRepository.existsByCodeAndSoftDeleteFalse(code);
    }

    public Mono<AcademicYear> getLatestAcademicYear() {
        return academicYearRepository.findTopBySoftDeleteFalseOrderByCreatedAtDesc()
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("No academic year found")
                ));
    }

    public Flux<AcademicYear> findAll() {
        return academicYearRepository.findAllBySoftDeleteFalse();
    }

    public Flux<AcademicYear> findAllDeleted() {
        return academicYearRepository.findAllBySoftDeleteTrue();
    }

    public Flux<AcademicYear> searchByCode(String partialCode) {
        return academicYearRepository.findAllByCodeContainingIgnoreCaseAndSoftDeleteFalse(partialCode);
    }

    public Mono<AcademicYear> update(AcademicYear academicYear) {
        log.info("Attempting to update AcademicYear ID: {}", academicYear.getAcademicYearId());
        return academicYearRepository.save(academicYear)
                .retryWhen(retryConfig.createRetrySpec("Update AcademicYear"))
                .doOnSuccess(updated -> log.info("Updated AcademicYear ID: {}", updated.getAcademicYearId()));
    }

    public Mono<Void> softDelete(UUID academicYearId) {
        log.info("Attempting to soft delete AcademicYear ID: {}", academicYearId);
        return academicYearRepository.findByAcademicYearIdAndSoftDeleteFalse(academicYearId)
                .flatMap(academicYear -> {
                    if (isCurrentYear(academicYear.getCode())) {
                        log.warn("Attempted to delete current year: {}", academicYear.getCode());
                        return Mono.error(new CurrentDeleteException(
                                "The current year cannot be deleted: " + academicYear.getCode()
                        ));
                    }
                    academicYear.setSoftDelete(true);
                    return academicYearRepository.save(academicYear);
                })
                .retryWhen(retryConfig.createRetrySpec("Soft Delete AcademicYear"))
                .then()
                .doOnSuccess(v -> log.info("Successfully soft deleted AcademicYear ID: {}", academicYearId));
    }

    private boolean isCurrentYear(String yearCode) {
        Year nowYear = Year.now();
        int academicYearStart = (LocalDate.now().getMonthValue() < 9) ?
                nowYear.getValue() - 1 : nowYear.getValue();
        return yearCode.equals("AY" + academicYearStart);
    }
}