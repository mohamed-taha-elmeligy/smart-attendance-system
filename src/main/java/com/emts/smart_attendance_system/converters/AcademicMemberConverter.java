package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.AcademicMemberMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestAcademicMember;
import com.emts.smart_attendance_system.dtos.responses.ResponseAcademicMember;
import com.emts.smart_attendance_system.entities.AcademicMember;
import com.emts.smart_attendance_system.services.AcademicMemberService;
import com.emts.smart_attendance_system.utils.BatchResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: AcademicMemberConverter.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 14/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@AllArgsConstructor
public class AcademicMemberConverter {
    private AcademicMemberService academicMemberService;
    private AcademicMemberMapper academicMemberMapper;

    public Mono<ResponseAcademicMember> addOne(RequestAcademicMember requestAcademicMember) {
        return academicMemberMapper.toResponseMono(
                academicMemberService.addOne(
                        academicMemberMapper.toEntity(requestAcademicMember)
                )
        );
    }

    public Mono<BatchResult> addAll(Flux<RequestAcademicMember> requestFlux) {
        return academicMemberService.addAll(
                requestFlux.map(academicMemberMapper::toEntity)
        );
    }

    public Mono<ResponseAcademicMember> findById(UUID academicMemberId) {
        return academicMemberMapper.toResponseMono(
                academicMemberService.findById(academicMemberId)
        );
    }

    public Mono<ResponseAcademicMember> findByUsername(String username) {
        return academicMemberMapper.toResponseMono(
                academicMemberService.findByUsername(username)
        );
    }

    public Mono<ResponseAcademicMember> findByEmail(String email) {
        return academicMemberMapper.toResponseMono(
                academicMemberService.findByEmail(email)
        );
    }

    public Flux<ResponseAcademicMember> findByUniversityNumber(String universityNumber) {
        return academicMemberMapper.toResponseFlux(
                academicMemberService.findByUniversityNumber(universityNumber)
        );
    }

    public Mono<Boolean> existsByUsername(String username) {
        return academicMemberService.existsByUsername(username);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return academicMemberService.existsByEmail(email);
    }

    public Mono<ResponseAcademicMember> update(UUID academicMemberId,
                                               RequestAcademicMember requestAcademicMember) {
        AcademicMember entity = academicMemberMapper.toEntity(requestAcademicMember);
        entity.setAcademicMemberId(academicMemberId);
        return academicMemberMapper.toResponseMono(
                academicMemberService.update(entity)
        );
    }

    public Flux<ResponseAcademicMember> findByRoleId(UUID roleId){
        return academicMemberService.findByRoleId(roleId)
                .map(academicMemberMapper::toResponse);
    }

    public Mono<Boolean> softDelete(UUID academicMemberId) {
        return academicMemberService.softDelete(academicMemberId);
    }
}