package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.UniversityMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestUniversity;
import com.emts.smart_attendance_system.dtos.responses.ResponseUniversity;
import com.emts.smart_attendance_system.entities.University;
import com.emts.smart_attendance_system.services.UniversityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: UniversityConverter.java
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
public class UniversityConverter {
    private UniversityService universityService;
    private UniversityMapper universityMapper;

    public Mono<ResponseUniversity> addOne(RequestUniversity requestUniversity) {
        return universityMapper.toResponseMono(
                universityService.addOne(
                        universityMapper.toEntity(requestUniversity)
                )
        );
    }

    public Mono<ResponseUniversity> findById(UUID universityId) {
        return universityMapper.toResponseMono(
                universityService.findById(universityId)
        );
    }

    public Mono<ResponseUniversity> findByName(String name) {
        return universityMapper.toResponseMono(
                universityService.findByName(name)
        );
    }

    public Mono<Boolean> existsByName(String name) {
        return universityService.existsByName(name);
    }

    public Flux<ResponseUniversity> findAll() {
        return universityMapper.toResponseFlux(
                universityService.findAll()
        );
    }

    public Flux<ResponseUniversity> searchByName(String partialName) {
        return universityMapper.toResponseFlux(
                universityService.searchByName(partialName)
        );
    }

    public Mono<ResponseUniversity> update(UUID universityId, RequestUniversity requestUniversity) {
        University entity = universityMapper.toEntity(requestUniversity);
        entity.setUniversitiesId(universityId);
        return universityMapper.toResponseMono(
                universityService.update(entity)
        );
    }

    public Mono<Void> delete(UUID universityId) {
        return universityService.delete(universityId);
    }
}