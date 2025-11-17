package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestAttendance;
import com.emts.smart_attendance_system.dtos.responses.ResponseAttendance;
import com.emts.smart_attendance_system.entities.Attendance;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: AttendanceMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * Description: Reactive mapper for Attendance entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AttendanceMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "attendanceId", ignore = true)
    @Mapping(target = "checkInTime", ignore = true)
    @Mapping(target = "present", ignore = true)
    @Mapping(target = "locationVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Attendance toEntity(RequestAttendance request);

    @Mapping(target = "presentedText", expression = "java(entity.getPresentedText())")
    @Mapping(target = "locationVerifiedText", expression = "java(entity.getLocationVerifiedText())")
    ResponseAttendance toResponse(Attendance entity);

    @Mapping(target = "attendanceId", ignore = true)
    @Mapping(target = "checkInTime", ignore = true)
    @Mapping(target = "present", ignore = true)
    @Mapping(target = "locationVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestAttendance request, @MappingTarget Attendance entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "attendanceId", ignore = true)
    @Mapping(target = "checkInTime", ignore = true)
    @Mapping(target = "present", ignore = true)
    @Mapping(target = "locationVerified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(RequestAttendance request, @MappingTarget Attendance entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<Attendance> toEntityMono(Mono<RequestAttendance> request) {
        return request.map(this::toEntity);
    }

    default Mono<Attendance> toEntityMono(RequestAttendance request) {
        return Mono.just(toEntity(request));
    }

    default Mono<ResponseAttendance> toResponseMono(Mono<Attendance> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseAttendance> toResponseMono(Attendance entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseAttendance> toResponseFlux(Flux<Attendance> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<Attendance> updateEntityFromRequestMono(
            Mono<RequestAttendance> request,
            Attendance entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<Attendance> partialUpdateMono(
            Mono<RequestAttendance> request,
            Attendance entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<Attendance> updateAndReturn(
            RequestAttendance request,
            Mono<Attendance> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<Attendance> partialUpdateAndReturn(
            RequestAttendance request,
            Mono<Attendance> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}
