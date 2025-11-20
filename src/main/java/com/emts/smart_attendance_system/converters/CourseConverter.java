package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.CourseMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestCourse;
import com.emts.smart_attendance_system.dtos.responses.ResponseCourse;
import com.emts.smart_attendance_system.entities.Course;
import com.emts.smart_attendance_system.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: CourseConverter.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 20/11/2025
 * Port Number: 8083
 * *******************************************************************
 */
@AllArgsConstructor
@Component
public class CourseConverter {
    private CourseService courseService;
    private CourseMapper courseMapper;

    public Mono<ResponseCourse> addOne(RequestCourse requestCourse){
        return courseMapper.toResponseMono(
                courseService.addOne(courseMapper.toEntity(requestCourse))
        );
    }

    public Mono<ResponseCourse> update (UUID courseId,RequestCourse requestCourse){
        Course course = courseMapper.toEntity(requestCourse);
        course.setCourseId(courseId);
        return courseMapper.toResponseMono(
                courseService.update(course)
        );
    }

    public Flux<ResponseCourse> findByUniversityId(UUID universityId){
        return courseMapper.toResponseFlux(courseService.findByUniversityId(universityId));
    }

    public Flux<ResponseCourse> findByAcademicYearId(UUID academicYearId){
        return courseMapper.toResponseFlux(courseService.findByAcademicYearId(academicYearId));
    }

    public Mono<ResponseCourse> softDelete(UUID courseId){
        return courseMapper.toResponseMono(courseService.softDelete(courseId));
    }

    public Mono<ResponseCourse> restore(UUID courseId){
        return courseMapper.toResponseMono(courseService.restore(courseId));
    }

    public Flux<ResponseCourse> searchByName(String name){
        return courseMapper.toResponseFlux(courseService.searchByName(name));
    }

    public Flux<ResponseCourse> searchByCode(String code){
        return courseMapper.toResponseFlux(courseService.searchByCode(code));
    }

    public Flux<ResponseCourse> search(String keyword){
        return courseMapper.toResponseFlux(courseService.search(keyword));
    }

    public Flux<ResponseCourse> findActive(){
        return courseMapper.toResponseFlux(courseService.findActive());
    }

    public Flux<ResponseCourse> findNotDeleted(){
        return courseMapper.toResponseFlux(courseService.findNotDeleted());
    }

    public Flux<ResponseCourse> findByUniversityAndYear(UUID universityId, UUID academicYearId){
        return courseMapper.toResponseFlux(courseService.findByUniversityAndYear(universityId,academicYearId));
    }

    public Mono<Boolean> existsByCode(String code){
        return courseService.existsByCodeAnd(code);
    }
}
