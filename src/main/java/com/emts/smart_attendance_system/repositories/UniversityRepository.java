package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.entities.University;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Repository
public interface UniversityRepository extends ReactiveCrudRepository<University, UUID> {

    Mono<University> findByUniversitiesId(UUID roleId);
    Mono<University> findByName(String name);
    Mono<Boolean> existsByName(String name);

    Flux<University> findAllByNameContainingIgnoreCase(String partialName);
}
