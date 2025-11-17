package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestLecture;
import com.emts.smart_attendance_system.dtos.responses.ResponseLecture;
import com.emts.smart_attendance_system.entities.Lecture;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: LectureMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * Description: Reactive mapper for Lecture entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LectureMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "lectureId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Lecture toEntity(RequestLecture request);

    @Mapping(target = "statusText", expression = "java(entity.getStatusText())")
    @Mapping(target = "lectureDeleteStatus", expression = "java(entity.getLectureDeleteStatus())")
    ResponseLecture toResponse(Lecture entity);

    @Mapping(target = "lectureId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestLecture request, @MappingTarget Lecture entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lectureId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(RequestLecture request, @MappingTarget Lecture entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<Lecture> toEntityMono(Mono<RequestLecture> request) {
        return request.map(this::toEntity);
    }

    default Mono<Lecture> toEntityMono(RequestLecture request) {
        return Mono.just(toEntity(request));
    }


    default Mono<ResponseLecture> toResponseMono(Mono<Lecture> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseLecture> toResponseMono(Lecture entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseLecture> toResponseFlux(Flux<Lecture> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<Lecture> updateEntityFromRequestMono(
            Mono<RequestLecture> request,
            Lecture entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<Lecture> partialUpdateMono(
            Mono<RequestLecture> request,
            Lecture entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<Lecture> updateAndReturn(
            RequestLecture request,
            Mono<Lecture> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<Lecture> partialUpdateAndReturn(
            RequestLecture request,
            Mono<Lecture> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}