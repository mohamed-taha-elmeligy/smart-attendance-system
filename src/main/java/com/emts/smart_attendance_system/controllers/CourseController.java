package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.CourseConverter;
import com.emts.smart_attendance_system.dtos.requests.RequestCourse;
import com.emts.smart_attendance_system.dtos.responses.ResponseCourse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: CourseController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 20/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@RestController
@RequestMapping("/api/v1/course")
@AllArgsConstructor
@Slf4j
public class CourseController {
    private CourseConverter courseConverter;

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseCourse>> addOne(
            @Valid @RequestBody RequestCourse requestCourse){
        log.debug("Adding new course: {}", requestCourse.getName());
        return courseConverter.addOne(requestCourse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error adding course: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseCourse>> update(
            @RequestParam @NonNull UUID courseId,
            @Valid @RequestBody RequestCourse requestCourse
            ){
        log.debug("Updating course: {}", courseId);
        return courseConverter.update(courseId,requestCourse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable ->{
                    log.error("Error updating course: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }


    @GetMapping("/search-university")
    public Flux<ResponseCourse> findByUniversityId(
            @RequestParam @NonNull UUID universityId){
        log.debug("Searching courses by University Id: {}", universityId);
        return courseConverter.findByUniversityId(universityId)
                .onErrorResume(throwable -> {
                    log.error("Error searching courses University Id: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search-academic-year")
    public Flux<ResponseCourse> findByAcademicYearId(
            @RequestParam @NonNull UUID academicYearId){
        log.debug("Searching courses by AcademicYear Id: {}", academicYearId);
        return courseConverter.findByAcademicYearId(academicYearId)
                .onErrorResume(throwable -> {
                    log.error("Error searching courses AcademicYear Id: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<ResponseCourse>> softDelete(
            @RequestParam @NonNull UUID courseId){
        log.debug("Deleting course by Id: {}", courseId);
        return courseConverter.softDelete(courseId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable ->{
                    log.error("Error deleting course by Id: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/undelete")
    public Mono<ResponseEntity<ResponseCourse>> restore(
            @RequestParam @NonNull UUID courseId){
        log.debug("Undeleting course by Id: {}", courseId);
        return courseConverter.restore(courseId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable ->{
                    log.error("Error undeleting course by Id: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("/search-name")
    public Flux<ResponseCourse> searchByName(
            @RequestParam @NotBlank String name){
        log.debug("Searching courses by name: {}", name);
        return courseConverter.searchByName( name)
                .onErrorResume(throwable -> {
                    log.error("Error searching courses by name: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search-code")
    public Flux<ResponseCourse> searchByCode(
            @RequestParam @NotBlank String code){
        log.debug("Searching courses by code: {}", code);
        return courseConverter.searchByCode(code)
                .onErrorResume(throwable -> {
                    log.error("Error searching courses by code: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search")
    public Flux<ResponseCourse> search(
            @RequestParam @NotBlank String keyword){
        log.debug("Searching courses by keyword: {}", keyword);
        return courseConverter.search(keyword)
                .onErrorResume(throwable -> {
                    log.error("Error searching courses by keyword: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search-active")
    public Flux<ResponseCourse> findActive(){
        log.debug("Searching courses by active");
        return courseConverter.findActive()
                .onErrorResume(throwable -> {
                    log.error("Error searching courses by active: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search-undeleted")
    public Flux<ResponseCourse> findNotDeleted(){
        log.debug("Searching courses by undeleted");
        return courseConverter.findNotDeleted()
                .onErrorResume(throwable -> {
                    log.error("Error searching courses by undeleted: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/search-university-academic-year")
    public Flux<ResponseCourse> findByUniversityAndYear(
            @RequestParam @NonNull UUID universityId,
            @RequestParam @NonNull UUID academicYearId){
        log.debug("Searching courses by universityId and academicYearId");
        return courseConverter.findByUniversityAndYear(universityId,academicYearId)
                .onErrorResume(throwable -> {
                    log.error("Error searching courses by universityId and academicYearId: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/exists-code")
    public Mono<ResponseEntity<Boolean>> existsByCode(
            @RequestParam @NotBlank String code){
        log.debug("Searching courses by code");
        return courseConverter.existsByCode(code)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error checking code existence: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(false));
                });
    }


}
