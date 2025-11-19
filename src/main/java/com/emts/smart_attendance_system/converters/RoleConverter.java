package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.RoleMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestRole;
import com.emts.smart_attendance_system.dtos.responses.ResponseRole;
import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: RoleConverter.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@AllArgsConstructor
public class RoleConverter {
    private RoleService roleService;
    private RoleMapper roleMapper;

    public Mono<ResponseRole> addOne(RequestRole requestRole) {
        return roleMapper.toResponseMono(
                roleService.addOne(
                        roleMapper.toEntity(requestRole)
                )
        );
    }

    public Mono<ResponseRole> findById(UUID roleId) {
        return roleMapper.toResponseMono(
                roleService.findById(roleId)
        );
    }

    public Mono<ResponseRole> findByName(String name) {
        return roleMapper.toResponseMono(
                roleService.findByName(name)
        );
    }

    public Mono<Boolean> existsByName(String name) {
        return roleService.existsByName(name);
    }

    public Flux<ResponseRole> findAll() {
        return roleMapper.toResponseFlux(
                roleService.findAll()
        );
    }

    public Flux<ResponseRole> findAllDeleted() {
        return roleMapper.toResponseFlux(
                roleService.findAllDeleted()
        );
    }

    public Flux<ResponseRole> searchByName(String partialName) {
        return roleMapper.toResponseFlux(
                roleService.searchByName(partialName)
        );
    }

    public Mono<ResponseRole> update(UUID roleId, RequestRole requestRole) {
        Role entity = roleMapper.toEntity(requestRole);
        entity.setRoleId(roleId);
        return roleMapper.toResponseMono(
                roleService.update(entity)
        );
    }

    public Mono<Void> softDelete(UUID roleId) {
        return roleService.softDelete(roleId);
    }
}