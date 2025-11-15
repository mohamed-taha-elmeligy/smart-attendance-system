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

    private final AcademicMemberRepository academicMemberRepository ;
    private final RetryConfig retryConfig ;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService ;
    private final Scheduler passwordScheduler;

    public AcademicMemberService(
            AcademicMemberRepository academicMemberRepository, RetryConfig retryConfig,
            PasswordEncoder passwordEncoder, RoleService roleService) {
        this.academicMemberRepository = academicMemberRepository;
        this.retryConfig = retryConfig;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.passwordScheduler = Schedulers.newParallel(
                "password-encoding",
                Runtime.getRuntime().availableProcessors()
        );
    }


    public Mono<AcademicMember> addOne(AcademicMember academicMember){
        log.info("Attempting to save a new AcademicMember with username: {}", academicMember.getUsername());

        return Mono.just(academicMember)
                .map(member -> {
                    String password = member.getPasswordHash();
                    member.setPasswordHash(passwordEncoder.encode(password));
                    return member;
                })
                .flatMap(academicMemberRepository::save)
                .retryWhen(retryConfig.createRetrySpec("Add AcademicMember"))
                .doOnSuccess(saved -> log.info("Successfully saved AcademicMember with ID: {}", saved.getAcademicMemberId()))
                .doOnError(error -> log.error("Failed to save AcademicMember: {}", error.getMessage()));
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

    public Mono<AcademicMember> findById(UUID academicMemberId){
        return academicMemberRepository
                .findByAcademicMemberIdAndSoftDeleteFalse(academicMemberId)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Find By ID"))
                .doOnError(error -> log.error("Error finding AcademicMember by ID: {}", error.getMessage()));
    }

    public Flux<AcademicMember> findByRoleId(UUID roleId){
        log.debug("Finding AcademicMembers with Role ID: {}", roleId);

        return academicMemberRepository
                .findByRoleIdAndSoftDeleteFalse(roleId)
                .retryWhen(retryConfig.createRetrySpec("AcademicMembers Find By Role ID"))
                .doOnNext(member ->
                        log.debug("Found member: {}", member.getUsername())
                )
                .doOnComplete(() ->
                        log.debug("Completed finding members for Role ID: {}", roleId)
                )
                .doOnError(error ->
                        log.error("Error finding AcademicMembers by Role ID {}: {}",
                                roleId, error.getMessage(), error)
                );
    }

    public Mono<AcademicMember> findByUsername(String username){
        return academicMemberRepository
                .findByUsernameAndSoftDeleteFalse(username)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Find By Username"))
                .doOnError(error -> log.error("Error finding AcademicMember by username: {}", error.getMessage()));
    }

    public Mono<AcademicMember> findByEmail(String email){
        return academicMemberRepository
                .findByEmailAndSoftDeleteFalse(email)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Find By Email"))
                .doOnError(error -> log.error("Error finding AcademicMember by email: {}", error.getMessage()));
    }

    public Flux<AcademicMember> findByUniversityNumber(String universityNumber){
        return academicMemberRepository
                .findByUniversityNumberAndSoftDeleteFalse(universityNumber)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Find By UniversityNumber"))
                .doOnError(error -> log.error("Error finding AcademicMember by university number: {}", error.getMessage()));
    }

    public Mono<AcademicMember> authenticate(String username, String passwordHash){
        return academicMemberRepository
                .findByUsernameAndPasswordHashAndSoftDeleteFalse(username, passwordHash)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Authenticate By Username and Password"))
                .doOnError(error -> log.error("Authentication failed: {}", error.getMessage()));
    }

    public Mono<AcademicMember> authenticateVerified(String username, String passwordHash){
        return academicMemberRepository
                .findByUsernameAndPasswordHashAndEmailVerifiedTrueAndSoftDeleteFalse(username, passwordHash)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Authenticate Verified By Username and Password"))
                .doOnError(error -> log.error("Verified authentication failed: {}", error.getMessage()));
    }

    public Mono<AcademicMember> authenticateUnverified(String username, String passwordHash){
        return academicMemberRepository
                .findByUsernameAndPasswordHashAndEmailVerifiedFalseAndSoftDeleteFalse(username, passwordHash)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Authenticate Unverified By Username and Password"))
                .doOnError(error -> log.error("Unverified authentication failed: {}", error.getMessage()));
    }

    public Mono<Boolean> existsByUsername(String username){
        return academicMemberRepository
                .existsByUsernameAndSoftDeleteFalse(username)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Exists By Username"))
                .doOnError(error -> log.error("Error checking username existence: {}", error.getMessage()));
    }

    public Mono<Boolean> existsByEmail(String email){
        return academicMemberRepository
                .existsByEmailAndSoftDeleteFalse(email)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Exists By Email"))
                .doOnError(error -> log.error("Error checking email existence: {}", error.getMessage()));
    }


    public Mono<Boolean> existsByUsernameAndUniversityNumber(String username, String universityNumber){
        return academicMemberRepository
                .existsByUsernameAndUniversityNumberAndSoftDeleteFalse(username, universityNumber)
                .retryWhen(retryConfig.createRetrySpec("AcademicMember Exists By Username and UniversityNumber"))
                .doOnError(error -> log.error("Error checking username and university number existence: {}", error.getMessage()));
    }

    public Mono<AcademicMember> update(AcademicMember academicMember){
        return academicMemberRepository
                .save(academicMember)
                .retryWhen(retryConfig.createRetrySpec("Update AcademicMember"))
                .doOnSuccess(updated -> log.info("Successfully updated AcademicMember with ID: {}", updated.getAcademicMemberId()))
                .doOnError(error -> log.error("Failed to update AcademicMember: {}", error.getMessage()));
    }

    public Mono<Boolean> softDelete(UUID academicMemberId) {
        log.info("Attempting to soft delete AcademicMember: {}", academicMemberId);

        return academicMemberRepository
                .findByAcademicMemberIdAndSoftDeleteFalse(academicMemberId)
                .flatMap(member ->
                        roleService.findById(member.getRoleId())
                                .flatMap(role -> {
                                    if (RoleData.DEVELOPER.name().equals(role.getName())) {
                                        log.warn("Cannot delete DEVELOPER role member: {}", academicMemberId);
                                        return Mono.just(false);
                                    }

                                    member.setSoftDelete(true);
                                    return academicMemberRepository.save(member)
                                            .thenReturn(true);
                                })
                )
                .defaultIfEmpty(false)
                .doOnSuccess(result ->
                        log.info("Soft delete result for {}: {}", academicMemberId, result)
                )
                .doOnError(error ->
                        log.error("Error while soft deleting AcademicMember {}: {}", academicMemberId, error.getMessage())
                );
    }

}
