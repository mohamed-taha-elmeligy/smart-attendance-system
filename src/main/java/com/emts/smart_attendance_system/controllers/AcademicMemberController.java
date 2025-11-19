package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.AcademicMemberConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestAcademicMember;
import com.emts.smart_attendance_system.dtos.responses.BatchResponse;
import com.emts.smart_attendance_system.dtos.responses.ResponseAcademicMember;
import com.emts.smart_attendance_system.security.CustomUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.security.core.context.SecurityContext;
import reactor.util.annotation.NonNull;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * *******************************************************************
 * File: AcademicMemberController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 14/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
@Slf4j
public class AcademicMemberController {
    private AcademicMemberConverter academicMemberConverter;
    private static final int MAX_BATCH_SIZE = 10000;
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseAcademicMember>> add(
            @RequestBody @Valid RequestAcademicMember academicMember) {
        log.debug("Adding new academic member: {}", academicMember.getUsername());
        return academicMemberConverter.addOne(academicMember)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)))
                .onErrorResume(e -> {
                    log.error("Error adding academic member: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                });
    }

    @PostMapping("/batch")
    public Mono<ResponseEntity<BatchResponse>> addAll(
            @RequestBody @Valid List<RequestAcademicMember> requestMembers) {
        log.debug("Processing batch of {} members", requestMembers.size());

        if (requestMembers.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest()
                    .body(new BatchResponse(0, 0, "Batch cannot be empty")));
        }

        if (requestMembers.size() > MAX_BATCH_SIZE) {
            return Mono.just(ResponseEntity.badRequest()
                    .body(new BatchResponse(0, requestMembers.size(),
                            "Batch size exceeds maximum limit of " + MAX_BATCH_SIZE)));
        }

        int totalCount = requestMembers.size();

        return academicMemberConverter.addAll(Flux.fromIterable(requestMembers))
                .map(result -> ResponseEntity.ok(
                        new BatchResponse(
                                result.getSuccessCount(),
                                result.getFailedCount(),
                                totalCount,
                                result.getSuccessCount() == totalCount ?
                                        "All members saved successfully" :
                                        "Partial success"
                        )
                ))
                .onErrorResume(e -> {
                    log.error("Critical error in batch processing", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new BatchResponse(0, totalCount, totalCount,
                                    "Internal error: " + e.getMessage())));
                })
                .timeout(Duration.ofMinutes(5))
                .doOnSuccess(response ->
                        log.info("Batch request completed: {} successful, {} failed",
                                response.getBody().getSuccessCount(),
                                response.getBody().getFailedCount())
                );
    }

    @GetMapping("/me")
    public Mono<ResponseAcademicMember> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(Authentication.class)
                .flatMap(auth -> {
                    if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated"));
                    }

                    log.debug("Fetching current user data for: {}", userDetails.getUsername());
                    return academicMemberConverter.findById(userDetails.id())
                            .doOnNext(member -> log.debug("Successfully retrieved user data for: {}", userDetails.getUsername()))
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
                });
    }

    @GetMapping("/search-username")
    public Mono<ResponseEntity<ResponseAcademicMember>> findByUsername(
            @RequestParam @NotBlank String username) {
        log.debug("Searching for academic member by username: {}", username);
        return academicMemberConverter.findByUsername(username)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding academic member by username: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/search-email")
    public Mono<ResponseEntity<ResponseAcademicMember>> findByEmail(
            @RequestParam @Email String email) {
        log.debug("Searching for academic member by email: {}", email);
        return academicMemberConverter.findByEmail(email)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    log.error("Error finding academic member by email: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/search-university-number")
    public Flux<ResponseAcademicMember> findByUniversityNumber(
            @RequestParam @NotBlank String universityNumber) {
        log.debug("Searching for academic members by university number: {}", universityNumber);
        return academicMemberConverter.findByUniversityNumber(universityNumber)
                .onErrorResume(e -> {
                    log.error("Error finding academic member by university number: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/exist-username")
    public Mono<ResponseEntity<Boolean>> existsByUsername(
            @RequestParam @NotBlank String username) {
        return academicMemberConverter.existsByUsername(username)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error checking username existence: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false));
                });
    }

    @GetMapping("/exist-email")
    public Mono<ResponseEntity<Boolean>> existsByEmail(
            @RequestParam @Email String email) {
        return academicMemberConverter.existsByEmail(email)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error checking email existence: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false));
                });
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseAcademicMember>> update(
            @RequestParam @NonNull UUID academicMemberId,
            @RequestBody @Valid RequestAcademicMember academicMember) {
        log.debug("Updating academic member: {}", academicMemberId);
        return academicMemberConverter.update(academicMemberId, academicMember)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)))
                .onErrorResume(e -> {
                    log.error("Error updating academic member: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                });
    }

    @GetMapping("/search-role")
    public Flux<ResponseAcademicMember> findByRoleId(
            @RequestParam @NonNull UUID roleId) {
        log.debug("Searching for members with role: {}", roleId);
        return academicMemberConverter.findByRoleId(roleId)
                .onErrorResume(e -> {
                    log.error("Error finding members by role ID: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Map<String, String>>> deleteMember(
            @RequestParam @NonNull UUID academicMemberId) {
        log.debug("Deleting academic member: {}", academicMemberId);
        return academicMemberConverter.softDelete(academicMemberId)
                .then(Mono.fromSupplier(() -> {
                    log.info("Successfully deleted academic member: {}", academicMemberId);
                    return ResponseEntity.ok(Map.of(
                            SUCCESS, "true",
                            MESSAGE, "Member deleted successfully"
                    ));
                }))
                .onErrorResume(IllegalArgumentException.class, ex -> {
                    log.warn("Failed to delete academic member {}: {}", academicMemberId, ex.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(Map.of(
                            SUCCESS, "false",
                            MESSAGE, ex.getMessage()
                    )));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("Unexpected error deleting academic member {}: {}", academicMemberId, ex.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(Map.of(
                            SUCCESS, "false",
                            MESSAGE, "Internal server error"
                    )));
                });
    }
}