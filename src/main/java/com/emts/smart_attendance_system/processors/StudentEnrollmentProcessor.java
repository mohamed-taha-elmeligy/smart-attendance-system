package com.emts.smart_attendance_system.processors;

import com.emts.smart_attendance_system.entities.Course;
import com.emts.smart_attendance_system.entities.Enrollment;
import com.emts.smart_attendance_system.services.CourseService;
import com.emts.smart_attendance_system.services.EnrollmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: StudentEnrollmentProcessor.java
 * Package: com.emts.smart_attendance_system.validation
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 10/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@Slf4j
@AllArgsConstructor
public class StudentEnrollmentProcessor {

    private final EnrollmentService enrollmentService;
    private final CourseService courseService;

    public void enrollStudentInAllCourses(UUID academicYear , UUID studentId){
        getCourses(academicYear)
                .flatMap(course ->
                        enrollmentService.addOne(
                                Enrollment.builder()
                                        .studentAcademicMember(studentId)
                                        .courseId(course.getCourseId())
                                        .build()
                                )
                                .doOnSuccess(success->
                                        log.info("Success add Student:{} to Course:{} ",
                                                studentId, course.getName()))
                                .doOnError(error->
                                        log.error("Error Add Student:{} to Course:{} ",
                                                studentId, course.getName()))
                                .onErrorResume(error -> {
                                    log.warn("Student {} failed to register for the course {}",
                                            studentId, course.getCourseId(), error);
                                    return Mono.empty();
                                })

                )
                .doOnComplete(()-> log.info("Registration has ended"))
                .doOnError(error -> log.error("General error: {}", error.getMessage()))
                .onErrorResume(error -> Mono.empty())
                .subscribe();
    }


    private Flux<Course> getCourses(UUID academicYear){
        return courseService.findByAcademicYearId(academicYear);
    }

}
