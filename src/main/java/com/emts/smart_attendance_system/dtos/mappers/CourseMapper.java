package com.emts.smart_attendance_system.dtos.mappers;

import com.emts.smart_attendance_system.dtos.requests.RequestCourse;
import com.emts.smart_attendance_system.dtos.responses.ResponseCourse;
import com.emts.smart_attendance_system.entities.Course;
import org.mapstruct.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: CourseMapper.java
 * Package: com.emts.smart_attendance_system.dtos.mappers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/11/2025
 * Port Number: 8083
 * Description: Reactive mapper for Course entity with R2DBC support
 * *******************************************************************
 */

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CourseMapper {

    // ===== Synchronous Mappings (for internal use) =====

    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Course toEntity(RequestCourse request);

    @Mapping(target = "courseDeleteStatus", expression = "java(entity.getCourseDeleteStatus())")
    ResponseCourse toResponse(Course entity);

    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RequestCourse request, @MappingTarget Course entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "softDelete", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(RequestCourse request, @MappingTarget Course entity);

    // ===== Reactive Helper Methods (Mono/Flux wrappers) =====

    default Mono<Course> toEntityMono(Mono<RequestCourse> request) {
        return request.map(this::toEntity);
    }

    default Mono<Course> toEntityMono(RequestCourse request) {
        return Mono.just(toEntity(request));
    }

    default Mono<ResponseCourse> toResponseMono(Mono<Course> entity) {
        return entity.map(this::toResponse);
    }

    default Mono<ResponseCourse> toResponseMono(Course entity) {
        return Mono.just(toResponse(entity));
    }

    default Flux<ResponseCourse> toResponseFlux(Flux<Course> entities) {
        return entities.map(this::toResponse);
    }

    default Mono<Course> updateEntityFromRequestMono(
            Mono<RequestCourse> request,
            Course entity) {
        return request.map(req -> {
            updateEntityFromRequest(req, entity);
            return entity;
        });
    }

    default Mono<Course> partialUpdateMono(
            Mono<RequestCourse> request,
            Course entity) {
        return request.map(req -> {
            partialUpdate(req, entity);
            return entity;
        });
    }

    default Mono<Course> updateAndReturn(
            RequestCourse request,
            Mono<Course> entityMono) {
        return entityMono.map(entity -> {
            updateEntityFromRequest(request, entity);
            return entity;
        });
    }

    default Mono<Course> partialUpdateAndReturn(
            RequestCourse request,
            Mono<Course> entityMono) {
        return entityMono.map(entity -> {
            partialUpdate(request, entity);
            return entity;
        });
    }
}