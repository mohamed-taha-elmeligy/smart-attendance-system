package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: RoleService.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 29/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
@AllArgsConstructor
@Slf4j
public class RoleService {

    private RoleRepository roleRepository;
    private RetryConfig retryConfig ;

    public Mono<Role> addOne(Role role){
        log.info("Attempting to save Role: {}", role.getName());
        return roleRepository.save(role)
                .retryWhen(retryConfig.createRetrySpec("Add Role"))
                .doOnSuccess(saved -> log.info("Saved Role ID: {}", saved.getRoleId()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("Role '{}' already exists. Skipping save.", role.getName());
                    return Mono.empty();
                });
    }

    public Mono<Boolean> addAll(Flux<Role> roleFlux){
        log.info("Starting batch save for Roles...");
        return roleRepository
                .saveAll(roleFlux)
                .then(Mono.fromCallable(() -> {
                    log.info("All Roles saved successfully.");
                    return true;
                }))
                .onErrorResume(error -> {
                    log.error("Error while saving Roles: {}", error.getMessage());
                    return Mono.just(false);
                });
    }

    public Mono<Role> findById(UUID roleId){
        return roleRepository.findByRoleIdAndSoftDeleteFalse(roleId)
                .retryWhen(retryConfig.createRetrySpec("Find Role By ID"))
                .doOnError(error -> log.error("Error finding Role by ID: {}", error.getMessage()));
    }

    public Mono<Role> findByName(String name){
        return roleRepository.findByNameAndSoftDeleteFalse(name)
                .retryWhen(retryConfig.createRetrySpec("Find Role By Name"))
                .doOnError(error -> log.error("Error finding Role by name: {}", error.getMessage()));
    }

    public Mono<Boolean> existsByName(String name){
        return roleRepository.existsByNameAndSoftDeleteFalse(name)
                .retryWhen(retryConfig.createRetrySpec("Check Role Existence"))
                .doOnError(error -> log.error("Error checking Role existence: {}", error.getMessage()));
    }

    public Flux<Role> findAll(){
        return roleRepository.findAllBySoftDeleteFalse()
                .retryWhen(retryConfig.createRetrySpec("Find All Roles"))
                .doOnError(error -> log.error("Error finding all Roles: {}", error.getMessage()));
    }

    public Flux<Role> findAllDeleted(){
        return roleRepository .findAllBySoftDeleteTrue()
                .retryWhen(retryConfig.createRetrySpec("Find All Roles is Deleted"))
                .doOnError(error -> log.error("Error finding deleted Roles: {}", error.getMessage()));
    }

    public Flux<Role> searchByName(String partialName){
        return roleRepository .findAllByNameContainingIgnoreCaseAndSoftDeleteFalse(partialName)
                .retryWhen(retryConfig.createRetrySpec("Find Role By Name"))
                .doOnError(error -> log.error("Error searching Roles by name: {}", error.getMessage()));
    }

    public Mono<Role> update(Role role){
        return roleRepository.save(role)
                .retryWhen(retryConfig.createRetrySpec("Update Role"))
                .doOnSuccess(updated -> log.info("Updated Role ID: {}", updated.getRoleId()))
                .doOnError(error -> log.error("Failed to update Role: {}", error.getMessage()));
    }

    public Mono<Void> softDelete(UUID roleId){
        return roleRepository.findByRoleIdAndSoftDeleteFalse(roleId)
                .flatMap(role -> {
                    role.setSoftDelete(true);
                    return roleRepository.save(role);
                })
                .retryWhen(retryConfig.createRetrySpec("Soft Delete Role"))
                .then()
                .doOnSuccess(v -> log.info("Soft deleted Role ID: {}", roleId))
                .doOnError(error -> log.error("Failed to soft delete Role: {}", error.getMessage()));
    }
}
