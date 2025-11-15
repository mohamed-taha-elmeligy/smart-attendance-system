package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.AcademicYear;
import com.emts.smart_attendance_system.repositories.AcademicYearRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<Boolean> addAll(Flux<AcademicYear> academicYearFlux) {
        log.info("Starting batch save for AcademicYears...");
        return academicYearRepository
                .saveAll(academicYearFlux)
                .then(Mono.fromCallable(() -> {
                    log.info("All AcademicYears saved successfully.");
                    return true;
                }))
                .onErrorResume(error -> {
                    log.error("Error while saving AcademicYears: {}", error.getMessage());
                    return Mono.just(false);
                });
    }

    public Mono<AcademicYear> findById(UUID academicYearId) {
        return academicYearRepository.findByAcademicYearIdAndSoftDeleteFalse(academicYearId)
                .retryWhen(retryConfig.createRetrySpec("Find AcademicYear By ID"))
                .doOnError(error -> log.error("Error finding AcademicYear by ID: {}", error.getMessage()));
    }

    public Mono<AcademicYear> findByCode(String code) {
        return academicYearRepository.findByCodeAndSoftDeleteFalse(code)
                .retryWhen(retryConfig.createRetrySpec("Find AcademicYear By Code"))
                .doOnError(error -> log.error("Error finding AcademicYear by code: {}", error.getMessage()));
    }

    public Mono<Boolean> existsByCode(String code) {
        return academicYearRepository.existsByCodeAndSoftDeleteFalse(code)
                .retryWhen(retryConfig.createRetrySpec("Check AcademicYear Existence"))
                .doOnError(error -> log.error("Error checking AcademicYear existence: {}", error.getMessage()));
    }

    public Mono<AcademicYear> getLatestAcademicYear() {
        return academicYearRepository.findTopBySoftDeleteFalseOrderByCreatedAtDesc()
                .retryWhen(retryConfig.createRetrySpec("Fetch latest AcademicYear"))
                .doOnError(error -> log.error("Error fetching latest AcademicYear: {}", error.getMessage()));
    }


    public Flux<AcademicYear> findAll() {
        return academicYearRepository.findAllBySoftDeleteFalse()
                .retryWhen(retryConfig.createRetrySpec("Find All AcademicYears"))
                .doOnError(error -> log.error("Error finding all AcademicYears: {}", error.getMessage()));
    }

    public Flux<AcademicYear> findAllDeleted() {
        return academicYearRepository.findAllBySoftDeleteTrue()
                .retryWhen(retryConfig.createRetrySpec("Find All Deleted AcademicYears"))
                .doOnError(error -> log.error("Error finding deleted AcademicYears: {}", error.getMessage()));
    }

    public Flux<AcademicYear> searchByCode(String partialCode) {
        return academicYearRepository.findAllByCodeContainingIgnoreCaseAndSoftDeleteFalse(partialCode)
                .retryWhen(retryConfig.createRetrySpec("Search AcademicYear By Code"))
                .doOnError(error -> log.error("Error searching AcademicYears by code: {}", error.getMessage()));
    }

    public Mono<AcademicYear> update(AcademicYear academicYear) {
        return academicYearRepository.save(academicYear)
                .retryWhen(retryConfig.createRetrySpec("Update AcademicYear"))
                .doOnSuccess(updated -> log.info("Updated AcademicYear ID: {}", updated.getAcademicYearId()))
                .doOnError(error -> log.error("Failed to update AcademicYear: {}", error.getMessage()));
    }

    public Mono<Void> softDelete(UUID academicYearId) {
        return academicYearRepository.findByAcademicYearIdAndSoftDeleteFalse(academicYearId)
                .flatMap(academicYear -> {
                    academicYear.setSoftDelete(true);
                    return academicYearRepository.save(academicYear);
                })
                .retryWhen(retryConfig.createRetrySpec("Soft Delete AcademicYear"))
                .then()
                .doOnSuccess(v -> log.info("Soft deleted AcademicYear ID: {}", academicYearId))
                .doOnError(error -> log.error("Failed to soft delete AcademicYear: {}", error.getMessage()));
    }
}
