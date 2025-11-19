package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.AcademicYearConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestAcademicYear;
import com.emts.smart_attendance_system.dtos.responses.ResponseAcademicYear;
import com.emts.smart_attendance_system.exceptions.exception.CurrentDeleteException;
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

import java.util.Map;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AcademicYearController.java
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
@RequestMapping("/api/v1/academic-year")
@Slf4j
public class AcademicYearController {
    private AcademicYearConverter academicYearConverter;
    private static final String SUCCESS ="success";
    private static final String MESSAGE ="message";


    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseAcademicYear>> add(
            @RequestBody @Valid RequestAcademicYear academicYear) {
        log.debug("Adding new academic year: {}", academicYear.getCode());
        return academicYearConverter.addOne(academicYear)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null)))
                .onErrorResume(e -> {
                    log.error("Error adding academic year: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @GetMapping("/{academicYearId}")
    public Mono<ResponseEntity<ResponseAcademicYear>> findById(
            @PathVariable @NonNull UUID academicYearId) {
        log.debug("Fetching academic year by ID: {}", academicYearId);
        return academicYearConverter.findById(academicYearId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding academic year by ID: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/search-code")
    public Mono<ResponseEntity<ResponseAcademicYear>> findByCode(
            @RequestParam @NotBlank String code) {
        log.debug("Searching academic year by code: {}", code);
        return academicYearConverter.findByCode(code)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding academic year by code: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/exist-code")
    public Mono<ResponseEntity<Boolean>> existsByCode(
            @RequestParam @NotBlank String code) {
        log.debug("Checking if academic year code exists: {}", code);
        return academicYearConverter.existsByCode(code)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error checking code existence: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(false));
                });
    }

    @GetMapping("/latest")
    public Mono<ResponseEntity<ResponseAcademicYear>> getLatestAcademicYear() {
        log.debug("Fetching latest academic year");
        return academicYearConverter.getLatestAcademicYear()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error fetching latest academic year: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/all")
    public Flux<ResponseAcademicYear> findAll() {
        log.debug("Fetching all academic years");
        return academicYearConverter.findAll()
                .onErrorResume(e -> {
                    log.error("Error fetching all academic years: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/all-deleted")
    public Flux<ResponseAcademicYear> findAllDeleted() {
        log.debug("Fetching all deleted academic years");
        return academicYearConverter.findAllDeleted()
                .onErrorResume(e -> {
                    log.error("Error fetching deleted academic years: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search")
    public Flux<ResponseAcademicYear> searchByCode(
            @RequestParam @NotBlank String code) {
        log.debug("Searching academic years by partial code: {}", code);
        return academicYearConverter.searchByCode(code)
                .onErrorResume(e -> {
                    log.error("Error searching academic years by code: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseAcademicYear>> update(
            @RequestParam @NonNull UUID academicYearId,
            @RequestBody @Valid RequestAcademicYear academicYear) {
        log.debug("Updating academic year: {}", academicYearId);
        return academicYearConverter.update(academicYearId, academicYear)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null)))
                .onErrorResume(e -> {
                    log.error("Error updating academic year: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null));
                });
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Map<String, String>>> softDelete(
            @RequestParam @NonNull UUID academicYearId) {
        log.debug("Soft deleting academic year: {}", academicYearId);
        return academicYearConverter.softDelete(academicYearId)
                .then(Mono.fromSupplier(() -> {
                    log.info("Successfully soft deleted academic year: {}", academicYearId);
                    return ResponseEntity.ok(Map.of(
                            SUCCESS, "true",
                            MESSAGE, "Academic year deleted successfully"
                    ));
                }))
                .onErrorResume(CurrentDeleteException.class, ex -> {
                    log.warn("Failed to soft delete academic year {}: {}", academicYearId, ex.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Map.of(
                            SUCCESS, "false",
                            MESSAGE, ex.getMessage()
                    )));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("Unexpected error deleting academic year {}: {}", academicYearId, ex.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(Map.of(
                            SUCCESS, "false",
                            MESSAGE, "Internal server error"
                    )));
                });
    }

}