package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.UniversityConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestUniversity;
import com.emts.smart_attendance_system.dtos.responses.ResponseUniversity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.UUID;

/**
 * *******************************************************************
 * File: UniversityController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/university")
@Slf4j
public class UniversityController {
    private UniversityConverter universityConverter;

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseUniversity>> add(
            @RequestBody @Valid RequestUniversity university) {
        log.debug("Adding new university: {}", university.getName());
        return universityConverter.addOne(university)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null)))
                .onErrorResume(e -> {
                    log.error("Error adding university: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @GetMapping("/{universityId}")
    public Mono<ResponseEntity<ResponseUniversity>> findById(
            @PathVariable @NonNull UUID universityId) {
        log.debug("Fetching university by ID: {}", universityId);
        return universityConverter.findById(universityId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding university by ID: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/search-name")
    public Mono<ResponseEntity<ResponseUniversity>> findByName(
            @RequestParam @NotBlank String name) {
        log.debug("Searching university by name: {}", name);
        return universityConverter.findByName(name)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding university by name: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/exist-name")
    public Mono<ResponseEntity<Boolean>> existsByName(
            @RequestParam @NotBlank String name) {
        log.debug("Checking if university name exists: {}", name);
        return universityConverter.existsByName(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error checking name existence: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(false));
                });
    }

    @GetMapping("/all")
    public Flux<ResponseUniversity> findAll() {
        log.debug("Fetching all universities");
        return universityConverter.findAll()
                .onErrorResume(e -> {
                    log.error("Error fetching all universities: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search")
    public Flux<ResponseUniversity> searchByName(
            @RequestParam @NotBlank String name) {
        log.debug("Searching universities by partial name: {}", name);
        return universityConverter.searchByName(name)
                .onErrorResume(e -> {
                    log.error("Error searching universities by name: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseUniversity>> update(
            @RequestParam @NonNull UUID universityId,
            @RequestBody @Valid RequestUniversity university) {
        log.debug("Updating university: {}", universityId);
        return universityConverter.update(universityId, university)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null)))
                .onErrorResume(e -> {
                    log.error("Error updating university: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Void>> delete(
            @RequestParam @NonNull UUID universityId) {
        log.debug("Deleting university: {}", universityId);
        return universityConverter.delete(universityId)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    log.error("Error deleting university: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}