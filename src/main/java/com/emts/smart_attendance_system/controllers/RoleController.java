package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.RoleConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestRole;
import com.emts.smart_attendance_system.dtos.responses.BatchResponse;
import com.emts.smart_attendance_system.dtos.responses.ResponseRole;
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

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * *******************************************************************
 * File: RoleController.java
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
@RequestMapping("/api/v1/role")
@Slf4j
public class RoleController {
    private RoleConverter roleConverter;
    private static final int MAX_BATCH_SIZE = 10000;

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseRole>> add(
            @RequestBody @Valid RequestRole role) {
        log.debug("Adding new role: {}", role.getName());
        return roleConverter.addOne(role)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null)))
                .onErrorResume(e -> {
                    log.error("Error adding role: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @PostMapping("/batch")
    public Mono<ResponseEntity<BatchResponse>> addAll(
            @RequestBody @Valid List<RequestRole> requestRoles) {
        log.info("Received batch request with {} roles", requestRoles.size());

        if (requestRoles.isEmpty()) {
            log.warn("Batch request received with empty list");
            return Mono.just(ResponseEntity.badRequest()
                    .body(new BatchResponse(0, 0, "Batch cannot be empty")));
        }

        if (requestRoles.size() > MAX_BATCH_SIZE) {
            log.warn("Batch size exceeds maximum limit: {} > {}", requestRoles.size(), MAX_BATCH_SIZE);
            return Mono.just(ResponseEntity.badRequest()
                    .body(new BatchResponse(0, requestRoles.size(),
                            "Batch size exceeds maximum limit of " + MAX_BATCH_SIZE)));
        }

        int totalCount = requestRoles.size();

        return roleConverter.addAll(Flux.fromIterable(requestRoles))
                .map(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        log.info("Batch processing completed successfully. Total: {}", totalCount);
                        return ResponseEntity.ok(
                                new BatchResponse(totalCount, 0, totalCount,
                                        "All roles saved successfully"));
                    } else {
                        log.warn("Batch processing completed with failures. Total: {}", totalCount);
                        return ResponseEntity.ok(
                                new BatchResponse(0, totalCount, totalCount,
                                        "Failed to save roles"));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Critical error in batch processing: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new BatchResponse(0, totalCount, totalCount,
                                    "Internal error: " + e.getMessage())));
                })
                .timeout(Duration.ofMinutes(5));
    }

    @GetMapping("/{roleId}")
    public Mono<ResponseEntity<ResponseRole>> findById(
            @PathVariable @NonNull UUID roleId) {
        log.debug("Fetching role by ID: {}", roleId);
        return roleConverter.findById(roleId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding role by ID: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/search-name")
    public Mono<ResponseEntity<ResponseRole>> findByName(
            @RequestParam @NotBlank String name) {
        log.debug("Searching role by name: {}", name);
        return roleConverter.findByName(name)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding role by name: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/exist-name")
    public Mono<ResponseEntity<Boolean>> existsByName(
            @RequestParam @NotBlank String name) {
        log.debug("Checking if role name exists: {}", name);
        return roleConverter.existsByName(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error checking name existence: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(false));
                });
    }

    @GetMapping("/all")
    public Flux<ResponseRole> findAll() {
        log.debug("Fetching all roles");
        return roleConverter.findAll()
                .onErrorResume(e -> {
                    log.error("Error fetching all roles: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/all-deleted")
    public Flux<ResponseRole> findAllDeleted() {
        log.debug("Fetching all deleted roles");
        return roleConverter.findAllDeleted()
                .onErrorResume(e -> {
                    log.error("Error fetching deleted roles: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search")
    public Flux<ResponseRole> searchByName(
            @RequestParam @NotBlank String name) {
        log.debug("Searching roles by partial name: {}", name);
        return roleConverter.searchByName(name)
                .onErrorResume(e -> {
                    log.error("Error searching roles by name: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseRole>> update(
            @RequestParam @NonNull UUID roleId,
            @RequestBody @Valid RequestRole role) {
        log.debug("Updating role: {}", roleId);
        return roleConverter.update(roleId, role)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null)))
                .onErrorResume(e -> {
                    log.error("Error updating role: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Void>> softDelete(
            @RequestParam @NonNull UUID roleId) {
        log.debug("Soft deleting role: {}", roleId);
        return roleConverter.softDelete(roleId)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    log.error("Error soft deleting role: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}