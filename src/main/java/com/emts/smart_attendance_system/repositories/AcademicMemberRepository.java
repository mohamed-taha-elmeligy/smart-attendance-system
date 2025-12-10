package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.AcademicMember;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: AcademicMemberRepository.java
 * Package: com.emts.smart_attendance_system.repositories
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 29/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Repository
public interface AcademicMemberRepository extends ReactiveCrudRepository<AcademicMember, UUID> {

    Flux<AcademicMember> findByRoleIdAndSoftDeleteFalse(UUID roleId);

    Mono<AcademicMember> findByAcademicMemberIdAndSoftDeleteFalse(UUID academicMemberId);
    Mono<AcademicMember> findByUsernameAndPasswordHashAndSoftDeleteFalse(String username, String passwordHash);
    Mono<AcademicMember> findByUsernameAndSoftDeleteFalse(String username);
    Flux<AcademicMember> findByUniversityNumberAndSoftDeleteFalse(String universityNumber);
    Mono<AcademicMember> findByEmailAndSoftDeleteFalse(String email);
    Mono<AcademicMember> findByUsernameAndPasswordHashAndEmailVerifiedTrueAndSoftDeleteFalse(String username, String passwordHash);
    Mono<AcademicMember> findByUsernameAndPasswordHashAndEmailVerifiedFalseAndSoftDeleteFalse(String username, String passwordHash);

    Mono<Boolean> existsByUsernameAndSoftDeleteFalse(String username);
    Mono<Boolean> existsByEmailAndSoftDeleteFalse(String email);
    Mono<Boolean> existsByUsernameAndUniversityNumberAndSoftDeleteFalse(String username, String universityNumber);

    Flux<AcademicMember> findByAcademicYearIdAndRoleIdAndSoftDeleteFalse(UUID academicYearId,UUID roleId);


}
