package com.emts.smart_attendance_system.processors;

import com.emts.smart_attendance_system.entities.Course;
import com.emts.smart_attendance_system.entities.Enrollment;
import com.emts.smart_attendance_system.enums.RoleData;
import com.emts.smart_attendance_system.services.CourseService;
import com.emts.smart_attendance_system.services.EnrollmentService;
import com.emts.smart_attendance_system.services.RoleService;
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
    private final RoleService roleService;

    public void enrollStudentInAllCourses(UUID academicYear , UUID studentId , UUID roleId){
        roleService.findById(roleId)
                .flatMapMany(role -> {
                    if (!role.getName().equals(RoleData.STUDENT.name())) {
                        return Flux.empty();
                    }
                    return getCourses(academicYear);
                })
                .flatMap(course ->
                        enrollmentService.addOne(
                                        Enrollment.builder()
                                                .studentAcademicMember(studentId)
                                                .courseId(course.getCourseId())
                                                .build()
                                )
                                .doOnSuccess(success ->
                                        log.info("Success add Student:{} to Course:{} ",
                                                studentId, course.getName()))
                                .doOnError(error ->
                                        log.error("Error Add Student:{} to Course:{} ",
                                                studentId, course.getName()))
                                .onErrorResume(error -> {
                                    log.warn("Student {} failed to register for the course {}",
                                            studentId, course.getCourseId(), error);
                                    return Mono.empty();
                                })
                )
                .then()
                .doOnTerminate(() -> log.info("Registration has ended"));
    }



    private Flux<Course> getCourses(UUID academicYear){
        return courseService.findByAcademicYearId(academicYear);
    }

}
