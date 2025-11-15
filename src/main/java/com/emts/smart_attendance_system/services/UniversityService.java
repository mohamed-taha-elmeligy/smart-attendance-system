package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.University;
import com.emts.smart_attendance_system.repositories.UniversityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: UniversityService.java
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
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final RetryConfig retryConfig;

    public Mono<University> addOne(University university) {
        log.info("Attempting to save University: {}", university.getName());
        return universityRepository.save(university)
                .retryWhen(retryConfig.createRetrySpec("Add University"))
                .doOnSuccess(saved -> log.info("Saved University ID: {}", saved.getUniversitiesId()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("University '{}' already exists. Skipping save.", university.getName());
                    return Mono.empty();
                });
    }

    public Mono<University> findById(UUID universityId) {
        return universityRepository.findByUniversitiesId(universityId)
                .retryWhen(retryConfig.createRetrySpec("Find University By ID"))
                .doOnError(error -> log.error("Error finding University by ID: {}", error.getMessage()));
    }

    public Mono<University> findByName(String name) {
        return universityRepository.findByName(name)
                .retryWhen(retryConfig.createRetrySpec("Find University By Name"))
                .doOnError(error -> log.error("Error finding University by name: {}", error.getMessage()));
    }

    public Mono<Boolean> existsByName(String name) {
        return universityRepository.existsByName(name)
                .retryWhen(retryConfig.createRetrySpec("Check University Existence"))
                .doOnError(error -> log.error("Error checking University existence: {}", error.getMessage()));
    }

    public Flux<University> findAll() {
        return universityRepository.findAll()
                .retryWhen(retryConfig.createRetrySpec("Find All Universities"))
                .doOnError(error -> log.error("Error finding all Universities: {}", error.getMessage()));
    }

    public Flux<University> searchByName(String partialName) {
        return universityRepository.findAllByNameContainingIgnoreCase(partialName)
                .retryWhen(retryConfig.createRetrySpec("Search University By Name"))
                .doOnError(error -> log.error("Error searching Universities by name: {}", error.getMessage()));
    }

    public Mono<University> update(University university) {
        return universityRepository.save(university)
                .retryWhen(retryConfig.createRetrySpec("Update University"))
                .doOnSuccess(updated -> log.info("Updated University ID: {}", updated.getUniversitiesId()))
                .doOnError(error -> log.error("Failed to update University: {}", error.getMessage()));
    }

    public Mono<Void> delete(UUID universityId) {
        log.info("Deleting University ID: {}", universityId);
        return universityRepository.findByUniversitiesId(universityId)
                .flatMap(universityRepository::delete)
                .retryWhen(retryConfig.createRetrySpec("Delete University"))
                .doOnSuccess(v -> log.info("Successfully deleted University ID: {}", universityId))
                .doOnError(error -> log.error("Failed to delete University: {}", error.getMessage()));
    }
}

