package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.entities.Lecture;
import com.emts.smart_attendance_system.dtos.mappers.LectureMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestLecture;
import com.emts.smart_attendance_system.dtos.responses.ResponseLecture;
import com.emts.smart_attendance_system.services.LectureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;


/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 21/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@Component
public class LectureConverter {
    private LectureService lectureService;
    private LectureMapper lectureMapper;

    public Mono<ResponseLecture> addOne(RequestLecture requestLecture) {
        return lectureMapper.toResponseMono(
                lectureService.addOne(lectureMapper.toEntity(requestLecture))
        );
    }

    public Mono<ResponseLecture> update(UUID lectureId, RequestLecture requestLecture) {
        Lecture lecture = lectureMapper.toEntity(requestLecture);
        lecture.setLectureId(lectureId);
        return lectureMapper.toResponseMono(
                lectureService.update(lecture)
        );
    }

    public Flux<ResponseLecture> findByCourseId(UUID courseId) {
        return lectureMapper.toResponseFlux(lectureService.findByCourseId(courseId));
    }

    public Flux<ResponseLecture> findActiveByCourseId(UUID courseId) {
        return lectureMapper.toResponseFlux(lectureService.findActiveByCourseId(courseId));
    }

    public Flux<ResponseLecture> findByInstructorId(UUID instructorAcademicMemberId) {
        return lectureMapper.toResponseFlux(lectureService.findByInstructorId(instructorAcademicMemberId));
    }

    public Flux<ResponseLecture> findActiveByInstructorId(UUID instructorAcademicMemberId) {
        return lectureMapper.toResponseFlux(lectureService.findActiveByInstructorId(instructorAcademicMemberId));
    }

    public Flux<ResponseLecture> findByCourseIdAndInstructorId(UUID courseId, UUID instructorAcademicMemberId) {
        return lectureMapper.toResponseFlux(lectureService.findByCourseIdAndInstructorId(courseId, instructorAcademicMemberId));
    }

    public Flux<ResponseLecture> findActiveByCourseIdAndInstructorId(UUID courseId, UUID instructorAcademicMemberId) {
        return lectureMapper.toResponseFlux(lectureService.findActiveByCourseIdAndInstructorId(courseId, instructorAcademicMemberId));
    }

    public Flux<ResponseLecture> findByLectureDate(LocalDate lectureDate) {
        return lectureMapper.toResponseFlux(lectureService.findByLectureDate(lectureDate));
    }

    public Flux<ResponseLecture> findActiveByLectureDate(LocalDate lectureDate) {
        return lectureMapper.toResponseFlux(lectureService.findActiveByLectureDate(lectureDate));
    }

    public Flux<ResponseLecture> findByLectureDateBetween(LocalDate startDate, LocalDate endDate) {
        return lectureMapper.toResponseFlux(lectureService.findByLectureDateBetween(startDate, endDate));
    }

    public Flux<ResponseLecture> findActiveByLectureDateBetween(LocalDate startDate, LocalDate endDate) {
        return lectureMapper.toResponseFlux(lectureService.findActiveByLectureDateBetween(startDate, endDate));
    }

    public Flux<ResponseLecture> findByInstructorIdAndLectureDate(UUID instructorAcademicMemberId, LocalDate lectureDate) {
        return lectureMapper.toResponseFlux(lectureService.findByInstructorIdAndLectureDate(instructorAcademicMemberId, lectureDate));
    }

    public Flux<ResponseLecture> findByInstructorIdAndLectureDateBetween(UUID instructorAcademicMemberId, LocalDate startDate, LocalDate endDate) {
        return lectureMapper.toResponseFlux(lectureService.findByInstructorIdAndLectureDateBetween(instructorAcademicMemberId, startDate, endDate));
    }

    public Flux<ResponseLecture> findByCourseIdAndLectureDate(UUID courseId, LocalDate lectureDate) {
        return lectureMapper.toResponseFlux(lectureService.findByCourseIdAndLectureDate(courseId, lectureDate));
    }

    public Flux<ResponseLecture> findByCourseIdAndLectureDateBetween(UUID courseId, LocalDate startDate, LocalDate endDate) {
        return lectureMapper.toResponseFlux(lectureService.findByCourseIdAndLectureDateBetween(courseId, startDate, endDate));
    }

    public Flux<ResponseLecture> findByRoom(String room) {
        return lectureMapper.toResponseFlux(lectureService.findByRoom(room));
    }

    public Flux<ResponseLecture> findActiveByRoom(String room) {
        return lectureMapper.toResponseFlux(lectureService.findActiveByRoom(room));
    }

    public Mono<ResponseLecture> activate(UUID lectureId) {
        return lectureMapper.toResponseMono(lectureService.activate(lectureId));
    }

    public Mono<ResponseLecture> deactivate(UUID lectureId) {
        return lectureMapper.toResponseMono(lectureService.deactivate(lectureId));
    }

    public Mono<ResponseLecture> softDelete(UUID lectureId) {
        return lectureMapper.toResponseMono(lectureService.softDelete(lectureId));
    }

    public Mono<ResponseLecture> restore(UUID lectureId) {
        return lectureMapper.toResponseMono(lectureService.restore(lectureId));
    }

    public Flux<ResponseLecture> findActive() {
        return lectureMapper.toResponseFlux(lectureService.findActive());
    }

    public Flux<ResponseLecture> findInactive() {
        return lectureMapper.toResponseFlux(lectureService.findInactive());
    }

    public Flux<ResponseLecture> findDeleted() {
        return lectureMapper.toResponseFlux(lectureService.findDeleted());
    }

    public Mono<Long> countByCourseId(UUID courseId) {
        return lectureService.countByCourseId(courseId);
    }

    public Mono<Long> countByInstructorId(UUID instructorAcademicMemberId) {
        return lectureService.countByInstructorId(instructorAcademicMemberId);
    }
}
