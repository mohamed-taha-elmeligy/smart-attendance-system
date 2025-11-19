package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.AcademicMember;
import com.emts.smart_attendance_system.enums.RoleData;
import com.emts.smart_attendance_system.repositories.AcademicMemberRepository;
import com.emts.smart_attendance_system.utils.BatchResult;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

/**
 * *******************************************************************
 * File: AcademicMemberService.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 29/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
@Slf4j
public class AcademicMemberService {

    private final AcademicMemberRepository academicMemberRepository;
    private final RetryConfig retryConfig;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final Scheduler passwordScheduler;

    public AcademicMemberService(
            AcademicMemberRepository academicMemberRepository,
            RetryConfig retryConfig,
            PasswordEncoder passwordEncoder,
            RoleService roleService) {
        this.academicMemberRepository = academicMemberRepository;
        this.retryConfig = retryConfig;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.passwordScheduler = Schedulers.newParallel(
                "password-encoding",
                Runtime.getRuntime().availableProcessors()
        );
    }

    public Mono<AcademicMember> addOne(AcademicMember academicMember) {
        log.info("Attempting to save a new AcademicMember with username: {}", academicMember.getUsername());
        return Mono.just(academicMember)
                .map(member -> {
                    member.setPasswordHash(passwordEncoder.encode(member.getPasswordHash()));
                    return member;
                })
                .flatMap(academicMemberRepository::save)
                .retryWhen(retryConfig.createRetrySpec("Add AcademicMember"))
                .doOnSuccess(saved -> log.info("Successfully saved AcademicMember with ID: {}", saved.getAcademicMemberId()));
    }

    @Transactional
    public Mono<BatchResult> addAll(Flux<AcademicMember> academicMemberFlux) {
        return academicMemberFlux
                .buffer(1000)
                .flatMap(batch ->
                                Flux.fromIterable(batch)
                                        .parallel(Runtime.getRuntime().availableProcessors())
                                        .runOn(passwordScheduler)
                                        .map(member -> {
                                            member.setPasswordHash(
                                                    passwordEncoder.encode(member.getPasswordHash())
                                            );
                                            return member;
                                        })
                                        .sequential()
                                        .collectList()
                                        .flatMapMany(academicMemberRepository::saveAll)
                                        .collectList()
                                        .map(saved -> new BatchResult(saved.size(), 0))
                                        .onErrorResume(e -> {
                                            log.error("Error saving batch of {} members", batch.size(), e);
                                            return Mono.just(new BatchResult(0, batch.size()));
                                        })
                        , 1)
                .reduce(new BatchResult(0, 0), (acc, result) ->
                        new BatchResult(
                                acc.getSuccessCount() + result.getSuccessCount(),
                                acc.getFailedCount() + result.getFailedCount()
                        )
                )
                .doOnSuccess(result ->
                        log.info("Batch save complete: {} succeeded, {} failed",
                                result.getSuccessCount(), result.getFailedCount())
                );
    }

    @PreDestroy
    public void cleanup() {
        passwordScheduler.dispose();
    }

    public Mono<AcademicMember> findById(UUID academicMemberId) {
        return academicMemberRepository
                .findByAcademicMemberIdAndSoftDeleteFalse(academicMemberId)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("AcademicMember not found with ID: {}", academicMemberId)
                ));
    }

    public Flux<AcademicMember> findByRoleId(UUID roleId) {
        log.debug("Finding AcademicMembers with Role ID: {}", roleId);
        return academicMemberRepository
                .findByRoleIdAndSoftDeleteFalse(roleId);
    }

    public Mono<AcademicMember> findByUsername(String username) {
        return academicMemberRepository
                .findByUsernameAndSoftDeleteFalse(username)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("AcademicMember not found with username: {}", username)
                ));
    }

    public Mono<AcademicMember> findByEmail(String email) {
        return academicMemberRepository
                .findByEmailAndSoftDeleteFalse(email)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("AcademicMember not found with email: {}", email)
                ));
    }

    public Flux<AcademicMember> findByUniversityNumber(String universityNumber) {
        return academicMemberRepository
                .findByUniversityNumberAndSoftDeleteFalse(universityNumber);
    }

    public Mono<AcademicMember> authenticate(String username, String passwordHash) {
        return academicMemberRepository
                .findByUsernameAndPasswordHashAndSoftDeleteFalse(username, passwordHash)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("Authentication failed for username: {}", username)
                ));
    }

    public Mono<AcademicMember> authenticateVerified(String username, String passwordHash) {
        return academicMemberRepository
                .findByUsernameAndPasswordHashAndEmailVerifiedTrueAndSoftDeleteFalse(username, passwordHash)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("Verified authentication failed for username: {}", username)
                ));
    }

    public Mono<AcademicMember> authenticateUnverified(String username, String passwordHash) {
        return academicMemberRepository
                .findByUsernameAndPasswordHashAndEmailVerifiedFalseAndSoftDeleteFalse(username, passwordHash)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("Unverified authentication failed for username: {}", username)
                ));
    }

    public Mono<Boolean> existsByUsername(String username) {
        return academicMemberRepository
                .existsByUsernameAndSoftDeleteFalse(username);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return academicMemberRepository
                .existsByEmailAndSoftDeleteFalse(email);
    }

    public Mono<Boolean> existsByUsernameAndUniversityNumber(String username, String universityNumber) {
        return academicMemberRepository
                .existsByUsernameAndUniversityNumberAndSoftDeleteFalse(username, universityNumber);
    }

    public Mono<AcademicMember> update(AcademicMember academicMember) {
        log.info("Attempting to update AcademicMember with ID: {}", academicMember.getAcademicMemberId());
        return academicMemberRepository
                .save(academicMember)
                .retryWhen(retryConfig.createRetrySpec("Update AcademicMember"))
                .doOnSuccess(updated -> log.info("Successfully updated AcademicMember with ID: {}", updated.getAcademicMemberId()));
    }

    public Mono<Void> softDelete(UUID academicMemberId) {
        log.info("Attempting to soft delete AcademicMember: {}", academicMemberId);
        return academicMemberRepository
                .findByAcademicMemberIdAndSoftDeleteFalse(academicMemberId)
                .flatMap(member ->
                        roleService.findById(member.getRoleId())
                                .flatMap(role -> {
                                    if (isSystemRole(role.getName())) {
                                        log.warn("Cannot delete system role member: {}", academicMemberId);
                                        return Mono.error(new IllegalArgumentException(
                                                "Cannot delete user with system role: " + role.getName()
                                        ));
                                    }
                                    member.setSoftDelete(true);
                                    return academicMemberRepository.save(member);
                                })
                )
                .retryWhen(retryConfig.createRetrySpec("Soft Delete AcademicMember"))
                .then()
                .doOnSuccess(v -> log.info("Successfully soft deleted AcademicMember: {}", academicMemberId));
    }

    private boolean isSystemRole(String roleName) {
        return RoleData.DEVELOPER.name().equals(roleName);
    }
}