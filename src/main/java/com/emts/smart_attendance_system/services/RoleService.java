package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.enums.RoleData;
import com.emts.smart_attendance_system.exceptions.exception.CurrentDeleteException;
import com.emts.smart_attendance_system.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
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
    private RetryConfig retryConfig;

    public Mono<Role> addOne(Role role) {
        log.info("Attempting to save Role: {}", role.getName());
        return roleRepository.save(role)
                .retryWhen(retryConfig.createRetrySpec("Add Role"))
                .doOnSuccess(saved -> log.info("Saved Role ID: {}", saved.getRoleId()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("Role '{}' already exists. Skipping save.", role.getName());
                    return Mono.empty();
                });
    }

    public Mono<Role> findById(UUID roleId) {
        return roleRepository.findByRoleIdAndSoftDeleteFalse(roleId)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("Role not found with ID: {}", roleId)
                ));
    }

    public Mono<Role> findByName(String name) {
        return roleRepository.findByNameAndSoftDeleteFalse(name)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        log.debug("Role not found with name: {}", name)
                ));
    }

    public Mono<Boolean> existsByName(String name) {
        return roleRepository.existsByNameAndSoftDeleteFalse(name);
    }

    public Flux<Role> findAll() {
        return roleRepository.findAllBySoftDeleteFalse();
    }

    public Flux<Role> findAllDeleted() {
        return roleRepository.findAllBySoftDeleteTrue();
    }

    public Flux<Role> searchByName(String partialName) {
        return roleRepository.findAllByNameContainingIgnoreCaseAndSoftDeleteFalse(partialName);
    }

    public Mono<Role> update(Role role) {
        log.info("Attempting to update Role ID: {}", role.getRoleId());
        return roleRepository.save(role)
                .retryWhen(retryConfig.createRetrySpec("Update Role"))
                .doOnSuccess(updated -> log.info("Updated Role ID: {}", updated.getRoleId()));
    }

    public Mono<Void> softDelete(UUID roleId) {
        log.info("Attempting to soft delete Role ID: {}", roleId);
        return roleRepository.findByRoleIdAndSoftDeleteFalse(roleId)
                .flatMap(role -> {
                    if (isSystemRole(role.getName())) {
                        log.warn("Attempted to delete system role: {}", role.getName());
                        return Mono.error(new CurrentDeleteException(
                                "The system role cannot be deleted: " + role.getName()
                        ));
                    }
                    role.setSoftDelete(true);
                    return roleRepository.save(role);
                })
                .retryWhen(retryConfig.createRetrySpec("Soft Delete Role"))
                .then()
                .doOnSuccess(v -> log.info("Soft deleted Role ID: {}", roleId));
    }


    private boolean isSystemRole(String roleName) {
        return Arrays.stream(RoleData.values())
                .anyMatch(role -> role.name().equals(roleName));
    }
}