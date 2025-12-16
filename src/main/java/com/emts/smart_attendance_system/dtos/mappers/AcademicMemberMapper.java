package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestAcademicMember;
import com.emts.smart_attendance_system.dtos.responses.ResponseAcademicMember;
import com.emts.smart_attendance_system.entities.AcademicMember;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: AcademicMemberMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * Description: Reactive mapper for AcademicMember entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AcademicMemberMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "academicMemberId", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    AcademicMember toEntity(RequestAcademicMember request);

    @Mapping(target = "emailVerificationStatus", expression = "java(entity.getEmailVerifiedText())")
    ResponseAcademicMember toResponse(AcademicMember entity);

    @Mapping(target = "academicMemberId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestAcademicMember request, @MappingTarget AcademicMember entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "academicMemberId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(RequestAcademicMember request, @MappingTarget AcademicMember entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<AcademicMember> toEntityMono(Mono<RequestAcademicMember> request) {
        return request.map(this::toEntity);
    }

    default Mono<AcademicMember> toEntityMono(RequestAcademicMember request) {
        return Mono.just(toEntity(request));
    }

    default Mono<ResponseAcademicMember> toResponseMono(Mono<AcademicMember> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseAcademicMember> toResponseMono(AcademicMember entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseAcademicMember> toResponseFlux(Flux<AcademicMember> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<AcademicMember> updateEntityFromRequestMono(
            Mono<RequestAcademicMember> request,
            AcademicMember entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<AcademicMember> partialUpdateMono(
            Mono<RequestAcademicMember> request,
            AcademicMember entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<AcademicMember> updateAndReturn(
            RequestAcademicMember request,
            Mono<AcademicMember> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<AcademicMember> partialUpdateAndReturn(
            RequestAcademicMember request,
            Mono<AcademicMember> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}