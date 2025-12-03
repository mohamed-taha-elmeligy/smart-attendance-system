package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.entities.Enrollment;
import com.emts.smart_attendance_system.dtos.mappers.EnrollmentMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestEnrollment;
import com.emts.smart_attendance_system.dtos.responses.ResponseEnrollment;
import com.emts.smart_attendance_system.services.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: EnrollmentConverter.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@Component
public class EnrollmentConverter {
    private EnrollmentService enrollmentService;
    private EnrollmentMapper enrollmentMapper;

    public Mono<ResponseEnrollment> addOne(RequestEnrollment requestEnrollment) {
        return enrollmentMapper.toResponseMono(
                enrollmentService.addOne(enrollmentMapper.toEntity(requestEnrollment))
        );
    }

    public Mono<ResponseEnrollment> update(UUID enrollmentId, RequestEnrollment requestEnrollment) {
        Enrollment enrollment = enrollmentMapper.toEntity(requestEnrollment);
        enrollment.setEnrollmentId(enrollmentId);
        return enrollmentMapper.toResponseMono(
                enrollmentService.update(enrollment)
        );
    }

    public Flux<ResponseEnrollment> findAllActive() {
        return enrollmentMapper.toResponseFlux(enrollmentService.findAllActive());
    }

    public Mono<ResponseEnrollment> findByIdActive(UUID enrollmentId) {
        return enrollmentMapper.toResponseMono(enrollmentService.findByIdActive(enrollmentId));
    }

    public Flux<ResponseEnrollment> findByCourseId(UUID courseId) {
        return enrollmentMapper.toResponseFlux(enrollmentService.findByCourseId(courseId));
    }

    public Flux<ResponseEnrollment> findByStudentAcademicMember(UUID studentAcademicMember) {
        return enrollmentMapper.toResponseFlux(enrollmentService.findByStudentAcademicMember(studentAcademicMember));
    }

    public Mono<ResponseEnrollment> findByExistsActive(UUID courseId, UUID studentAcademicMember) {
        return enrollmentMapper.toResponseMono(enrollmentService.findByExistsActive(courseId, studentAcademicMember));
    }

    public Mono<ResponseEnrollment> findByExists(UUID courseId, UUID studentAcademicMember) {
        return enrollmentMapper.toResponseMono(enrollmentService.findByExists(courseId, studentAcademicMember));
    }

    public Mono<Long> countByCourseId(UUID courseId) {
        return enrollmentService.countByCourseId(courseId);
    }

    public Mono<Long> countByStudentAcademicMember(UUID studentAcademicMember) {
        return enrollmentService.countByStudentAcademicMember(studentAcademicMember);
    }

    public Mono<Long> countAllActive() {
        return enrollmentService.countAllActive();
    }

    public Mono<Integer> softDeleteByCourseId(UUID courseId) {
        return enrollmentService.softDeleteByCourseId(courseId);
    }

    public Mono<Integer> softDeleteByStudentAcademicMember(UUID studentAcademicMember) {
        return enrollmentService.softDeleteByStudentAcademicMember(studentAcademicMember);
    }

    public Mono<Integer> softDeleteById(UUID enrollmentId) {
        return enrollmentService.softDeleteById(enrollmentId);
    }

    public Mono<Integer> restoreEnrollment(UUID enrollmentId) {
        return enrollmentService.restoreEnrollment(enrollmentId);
    }

    public Mono<Integer> restoreByCourseId(UUID courseId) {
        return enrollmentService.restoreByCourseId(courseId);
    }

    public Flux<ResponseEnrollment> findAllDeleted() {
        return enrollmentMapper.toResponseFlux(enrollmentService.findAllDeleted());
    }

    public Flux<ResponseEnrollment> findDeletedByCourseId(UUID courseId) {
        return enrollmentMapper.toResponseFlux(enrollmentService.findDeletedByCourseId(courseId));
    }
}