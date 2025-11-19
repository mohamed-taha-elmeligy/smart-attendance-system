package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.AcademicYearMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestAcademicYear;
import com.emts.smart_attendance_system.dtos.responses.ResponseAcademicYear;
import com.emts.smart_attendance_system.entities.AcademicYear;
import com.emts.smart_attendance_system.services.AcademicYearService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: AcademicYearConverter.java
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
public class AcademicYearConverter {
    private AcademicYearService academicYearService;
    private AcademicYearMapper academicYearMapper;

    public Mono<ResponseAcademicYear> addOne(RequestAcademicYear requestAcademicYear) {
        return academicYearMapper.toResponseMono(
                academicYearService.addOne(
                        academicYearMapper.toEntity(requestAcademicYear)
                )
        );
    }

    public Mono<ResponseAcademicYear> findById(UUID academicYearId) {
        return academicYearMapper.toResponseMono(
                academicYearService.findById(academicYearId)
        );
    }

    public Mono<ResponseAcademicYear> findByCode(String code) {
        return academicYearMapper.toResponseMono(
                academicYearService.findByCode(code)
        );
    }

    public Mono<Boolean> existsByCode(String code) {
        return academicYearService.existsByCode(code);
    }

    public Mono<ResponseAcademicYear> getLatestAcademicYear() {
        return academicYearMapper.toResponseMono(
                academicYearService.getLatestAcademicYear()
        );
    }

    public Flux<ResponseAcademicYear> findAll() {
        return academicYearMapper.toResponseFlux(
                academicYearService.findAll()
        );
    }

    public Flux<ResponseAcademicYear> findAllDeleted() {
        return academicYearMapper.toResponseFlux(
                academicYearService.findAllDeleted()
        );
    }

    public Flux<ResponseAcademicYear> searchByCode(String partialCode) {
        return academicYearMapper.toResponseFlux(
                academicYearService.searchByCode(partialCode)
        );
    }

    public Mono<ResponseAcademicYear> update(UUID academicYearId,
                                             RequestAcademicYear requestAcademicYear) {
        AcademicYear entity = academicYearMapper.toEntity(requestAcademicYear);
        entity.setAcademicYearId(academicYearId);
        return academicYearMapper.toResponseMono(
                academicYearService.update(entity)
        );
    }

    public Mono<Void> softDelete(UUID academicYearId) {
        return academicYearService.softDelete(academicYearId);
    }
}