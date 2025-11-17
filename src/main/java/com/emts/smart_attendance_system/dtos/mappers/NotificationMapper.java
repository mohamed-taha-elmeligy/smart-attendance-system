package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.responses.ResponseNotification;
import com.emts.smart_attendance_system.entities.Notification;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: NotificationMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * Description: Reactive mapper for Notification entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "readText", expression = "java(entity.getReadText())")
    ResponseNotification toResponse(Notification entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<ResponseNotification> toResponseMono(Mono<Notification> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseNotification> toResponseMono(Notification entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseNotification> toResponseFlux(Flux<Notification> entities) {
        return entities.map(this::toResponse);
    }
}