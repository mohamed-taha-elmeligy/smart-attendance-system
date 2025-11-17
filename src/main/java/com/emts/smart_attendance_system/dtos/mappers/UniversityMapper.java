package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestUniversity;
import com.emts.smart_attendance_system.dtos.responses.ResponseUniversity;
import com.emts.smart_attendance_system.entities.University;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: UniversityMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * Description: Reactive mapper for University entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UniversityMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "universitiesId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    University toEntity(RequestUniversity request);

    ResponseUniversity toResponse(University entity);

    @Mapping(target = "universitiesId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestUniversity request, @MappingTarget University entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "universitiesId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(RequestUniversity request, @MappingTarget University entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<University> toEntityMono(Mono<RequestUniversity> request) {
        return request.map(this::toEntity);
    }

    default Mono<University> toEntityMono(RequestUniversity request) {
        return Mono.just(toEntity(request));
    }

    default Mono<ResponseUniversity> toResponseMono(Mono<University> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseUniversity> toResponseMono(University entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseUniversity> toResponseFlux(Flux<University> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<University> updateEntityFromRequestMono(
            Mono<RequestUniversity> request,
            University entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<University> partialUpdateMono(
            Mono<RequestUniversity> request,
            University entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<University> updateAndReturn(
            RequestUniversity request,
            Mono<University> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<University> partialUpdateAndReturn(
            RequestUniversity request,
            Mono<University> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}