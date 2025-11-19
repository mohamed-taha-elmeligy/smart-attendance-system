package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.University;
import com.emts.smart_attendance_system.enums.UniversitiesData;
import com.emts.smart_attendance_system.exceptions.exception.CurrentDeleteException;
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
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("University not found with ID: {}", universityId)
                ));
    }

    public Mono<University> findByName(String name) {
        return universityRepository.findByName(name)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("University not found with name: {}", name)
                ));
    }

    public Mono<Boolean> existsByName(String name) {
        return universityRepository.existsByName(name);
    }

    public Flux<University> findAll() {
        return universityRepository.findAll();
    }

    public Flux<University> searchByName(String partialName) {
        return universityRepository.findAllByNameContainingIgnoreCase(partialName);
    }

    public Mono<University> update(University university) {
        log.info("Attempting to update University ID: {}", university.getUniversitiesId());
        return universityRepository.save(university)
                .retryWhen(retryConfig.createRetrySpec("Update University"))
                .doOnSuccess(updated -> log.info("Updated University ID: {}", updated.getUniversitiesId()));
    }

    public Mono<Void> delete(UUID universityId) {
        log.info("Attempting to delete University ID: {}", universityId);
        return universityRepository.findByUniversitiesId(universityId)
                .flatMap(university -> {
                    if (UniversitiesData.DEVELOPERS_UNIVERSITY.name().equals(university.getName())) {
                        log.warn("Attempted to delete system university: {}", university.getName());
                        return Mono.error(new CurrentDeleteException("The system role cannot be deleted:"+ university.getName()));
                    }
                    return universityRepository.delete(university);
                })
                .retryWhen(retryConfig.createRetrySpec("Delete University"))
                .then()
                .doOnSuccess(v -> log.info("Successfully deleted University ID: {}", universityId));
    }
}