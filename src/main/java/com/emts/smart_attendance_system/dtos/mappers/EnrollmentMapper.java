package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestEnrollment;
import com.emts.smart_attendance_system.dtos.responses.ResponseEnrollment;
import com.emts.smart_attendance_system.entities.Enrollment;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: EnrollmentMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * Description: Reactive mapper for Enrollment entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EnrollmentMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "enrollmentId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Enrollment toEntity(RequestEnrollment request);

    @Mapping(target = "enrollmentDeleteStatus", expression = "java(entity.getEnrollmentDeleteStatus())")
    ResponseEnrollment toResponse(Enrollment entity);

    @Mapping(target = "enrollmentId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestEnrollment request, @MappingTarget Enrollment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "enrollmentId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(RequestEnrollment request, @MappingTarget Enrollment entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<Enrollment> toEntityMono(Mono<RequestEnrollment> request) {
        return request.map(this::toEntity);
    }

    default Mono<Enrollment> toEntityMono(RequestEnrollment request) {
        return Mono.just(toEntity(request));
    }

    default Mono<ResponseEnrollment> toResponseMono(Mono<Enrollment> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseEnrollment> toResponseMono(Enrollment entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseEnrollment> toResponseFlux(Flux<Enrollment> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<Enrollment> updateEntityFromRequestMono(
            Mono<RequestEnrollment> request,
            Enrollment entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<Enrollment> partialUpdateMono(
            Mono<RequestEnrollment> request,
            Enrollment entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<Enrollment> updateAndReturn(
            RequestEnrollment request,
            Mono<Enrollment> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<Enrollment> partialUpdateAndReturn(
            RequestEnrollment request,
            Mono<Enrollment> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}