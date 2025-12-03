package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestQrCode;
import com.emts.smart_attendance_system.dtos.responses.ResponseQrCode;
import com.emts.smart_attendance_system.entities.QrCode;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: QrCodeMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 25/10/2025
 * Port Number: 8083
 * Description: Reactive mapper for QrCode entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface QrCodeMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "qrCodeId", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "activated", ignore = true)
    @Mapping(target = "expired", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "uuidTokenHash", ignore = true)
    QrCode toEntity(RequestQrCode request);

    @Mapping(target = "activatedStatus", expression = "java(entity.getActivatedStatus())")
    ResponseQrCode toResponse(QrCode entity);

    @Mapping(target = "qrCodeId", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "activated", ignore = true)
    @Mapping(target = "expired", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "uuidTokenHash", ignore = true)
    void updateEntityFromRequest(RequestQrCode request, @MappingTarget QrCode entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "qrCodeId", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "activated", ignore = true)
    @Mapping(target = "expired", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "uuidTokenHash", ignore = true)
    void partialUpdate(RequestQrCode request, @MappingTarget QrCode entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<QrCode> toEntityMono(Mono<RequestQrCode> request) {
        return request.map(this::toEntity);
    }

    default Mono<QrCode> toEntityMono(RequestQrCode request) {
        return Mono.just(toEntity(request));
    }

    default Mono<ResponseQrCode> toResponseMono(Mono<QrCode> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseQrCode> toResponseMono(QrCode entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseQrCode> toResponseFlux(Flux<QrCode> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<QrCode> updateEntityFromRequestMono(
            Mono<RequestQrCode> request,
            QrCode entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<QrCode> partialUpdateMono(
            Mono<RequestQrCode> request,
            QrCode entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<QrCode> updateAndReturn(
            RequestQrCode request,
            Mono<QrCode> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<QrCode> partialUpdateAndReturn(
            RequestQrCode request,
            Mono<QrCode> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}