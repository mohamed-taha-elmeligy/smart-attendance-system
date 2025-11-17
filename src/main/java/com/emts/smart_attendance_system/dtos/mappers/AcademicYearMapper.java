package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestAcademicYear;
import com.emts.smart_attendance_system.dtos.responses.ResponseAcademicYear;
import com.emts.smart_attendance_system.entities.AcademicYear;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: AcademicYearMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * Description: Reactive mapper for AcademicYear entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AcademicYearMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "academicYearId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AcademicYear toEntity(RequestAcademicYear request);

    @Mapping(target = "academicYearDeleteStatus", expression = "java(entity.getAcademicYearDeleteStatus())")
    ResponseAcademicYear toResponse(AcademicYear entity);

    @Mapping(target = "academicYearId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestAcademicYear request, @MappingTarget AcademicYear entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "academicYearId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(RequestAcademicYear request, @MappingTarget AcademicYear entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<AcademicYear> toEntityMono(Mono<RequestAcademicYear> request) {
        return request.map(this::toEntity);
    }

    default Mono<AcademicYear> toEntityMono(RequestAcademicYear request) {
        return Mono.just(toEntity(request));
    }

    default Mono<ResponseAcademicYear> toResponseMono(Mono<AcademicYear> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseAcademicYear> toResponseMono(AcademicYear entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseAcademicYear> toResponseFlux(Flux<AcademicYear> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<AcademicYear> updateEntityFromRequestMono(
            Mono<RequestAcademicYear> request,
            AcademicYear entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<AcademicYear> partialUpdateMono(
            Mono<RequestAcademicYear> request,
            AcademicYear entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<AcademicYear> updateAndReturn(
            RequestAcademicYear request,
            Mono<AcademicYear> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<AcademicYear> partialUpdateAndReturn(
            RequestAcademicYear request,
            Mono<AcademicYear> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}