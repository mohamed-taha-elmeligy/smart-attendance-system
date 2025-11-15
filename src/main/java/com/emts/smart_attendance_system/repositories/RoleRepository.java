package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: RoleRepository.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 29/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, UUID> {

    Mono<Role> findByRoleIdAndSoftDeleteFalse(UUID roleId);
    Mono<Role> findByNameAndSoftDeleteFalse(String name);
    Mono<Boolean> existsByNameAndSoftDeleteFalse(String name);

    Flux<Role> findAllBySoftDeleteFalse();
    Flux<Role> findAllBySoftDeleteTrue();
    Flux<Role> findAllByNameContainingIgnoreCaseAndSoftDeleteFalse(String partialName);

}
