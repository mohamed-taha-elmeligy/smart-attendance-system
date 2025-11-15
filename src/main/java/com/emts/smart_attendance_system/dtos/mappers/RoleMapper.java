package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestRole;
import com.emts.smart_attendance_system.dtos.responses.ResponseRole;
import com.emts.smart_attendance_system.entities.Role;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 27/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    // ===== Core MapStruct Methods (Blocking - MapStruct handles these) =====

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Role toEntity(RequestRole requestRole);

    @Mapping(target = "getRoleDeleteStatus", expression = "java(role.getRoleDeleteStatus())")
    ResponseRole toResponse(Role role);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestRole requestRole, @MappingTarget Role role);

    // ===== Reactive Wrapper Methods (Default - We implement these) =====

    default Mono<Role> toEntityMono(Mono<RequestRole> requestRoleMono) {
        return requestRoleMono.map(this::toEntity);
    }


    default Mono<ResponseRole> toResponseMono(Mono<Role> roleMono) {
        return roleMono.map(this::toResponse);
    }

    default Flux<ResponseRole> toResponseFlux(Flux<Role> roleFlux) {
        return roleFlux.map(this::toResponse);
    }

    default Mono<Role> updateEntityReactive(Mono<Role> roleMono, RequestRole requestRole) {
        return roleMono.doOnNext(role -> updateEntityFromRequest(requestRole, role));
    }

    default Mono<Role> updateEntityReactiveFull(Mono<Role> roleMono, Mono<RequestRole> requestRoleMono) {
        return Mono.zip(roleMono, requestRoleMono)
                .doOnNext(tuple -> updateEntityFromRequest(tuple.getT2(), tuple.getT1()))
                .map(Tuple2::getT1);
    }
}
