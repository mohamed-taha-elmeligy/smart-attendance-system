package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.Course;
import com.emts.smart_attendance_system.repositories.CourseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: CourseService.java
 * Package: com.emts.smart_attendance_system.services
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 20/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Service
@AllArgsConstructor
@Slf4j
public class CourseService {
    private CourseRepository courseRepository;
    private RetryConfig retryConfig;

    public Mono<Course> addOne(Course course) {
        log.info("Attempting to save Course: {}", course.getName());
        return courseRepository.existsByCodeAndUniversityIdAndAcademicYearIdAndSoftDeleteFalse(
                        course.getCode(),
                        course.getUniversityId(),
                        course.getAcademicYearId()
                )
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        log.warn("Course '{}' with code '{}' already exists. Skipping save.",
                                course.getName(), course.getCode());
                        return Mono.empty();
                    } else {
                        return courseRepository.save(course)
                                .retryWhen(retryConfig.createRetrySpec("Add Course"))
                                .doOnSuccess(saved -> log.info("Saved Course ID: {}", saved.getCourseId()));
                    }
                });
    }


    public Mono<Course> update (Course course){
        log.info("Attempting to update Course: {}", course.getCourseId());
        return courseRepository.save(course)
                .retryWhen(retryConfig.createRetrySpec("Update Course"))
                .doOnSuccess(saved-> log.info("Update Course ID: {}", saved.getCourseId()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("Course '{}' already exists. Skipping update.", course.getName());
                    return Mono.empty();
                });
    }

    public Mono<Course> findByCode(String code){
        return courseRepository.findByCodeAndSoftDeleteFalse(code)
                .switchIfEmpty(Mono.fromRunnable(()->
                        log.debug("Course not found with Code: {}", code)
                ));
    }

    public Flux<Course> findByUniversityId(UUID universityId){
        return courseRepository.findByUniversityIdAndSoftDeleteFalse(universityId);
    }

    public Flux<Course> findByAcademicYearId(UUID academicYearId){
        return courseRepository.findByAcademicYearIdAndSoftDeleteFalse(academicYearId);
    }
    public Flux<Course> findByInstructorId(UUID instructor){
        return courseRepository.findByInstructorAcademicMemberIdAndSoftDeleteFalse(instructor);
    }

    public Mono<Course> softDelete(UUID courseId){
        return courseRepository.softDelete(courseId)
                .retryWhen(retryConfig.createRetrySpec("Soft Delete Course"))
                .doOnSuccess(success-> log.info("Soft Delete Course ID: {}", success.getCourseId()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("Course '{}' already exists. Skipping soft delete.", courseId);
                    return Mono.empty();
                });
    }

    public Mono<Course> restore(UUID courseId){
        return courseRepository.restore(courseId)
                .retryWhen(retryConfig.createRetrySpec("Restore Course"))
                .doOnSuccess(success-> log.info("Restore Course ID: {}", success.getCourseId()))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("Course '{}' already exists. Skipping restore.", courseId);
                    return Mono.empty();
                });
    }

    public Flux<Course> searchByName(String name){
        return courseRepository.findByNameContainingIgnoreCaseAndSoftDeleteFalse(name);
    }

    public Flux<Course> searchByCode(String code){
        return courseRepository.findByCodeContainingIgnoreCaseAndSoftDeleteFalse(code);
    }

    public Flux<Course> search(String keyword){
        return courseRepository.search(keyword);
    }

    public Flux<Course> findActive(){
        return courseRepository.findActive();
    }

    public Flux<Course> findNotDeleted(){
        return courseRepository.findNotDeleted();
    }

    public Flux<Course> findByUniversityAndYear(UUID universityId, UUID academicYearId){
        return courseRepository.findByUniversityAndYearAndSoftDeleteFalse(universityId, academicYearId);
    }

    public Mono<Boolean> existsByCodeAnd(String code){
        return courseRepository.existsByCodeAndSoftDeleteFalse(code);
    }
}
