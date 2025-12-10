package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.entities.*;
import com.emts.smart_attendance_system.enums.RoleData;
import com.emts.smart_attendance_system.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.seeder
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 08/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j(topic = "SeederLogger")
@Component
@Order(8)
public class EnrollmentSetSeeder implements CommandLineRunner {

    private final AcademicMemberService academicMemberService;
    private final RoleService roleService;
    private final AcademicYearService academicYearService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public EnrollmentSetSeeder(AcademicMemberService academicMemberService,
                               RoleService roleService,
                               AcademicYearService academicYearService,
                               CourseService courseService,
                               EnrollmentService enrollmentService) {
        this.academicMemberService = academicMemberService;
        this.roleService = roleService;
        this.academicYearService = academicYearService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Enrollment seeder initialization...");
//        try {
//            createEnrollments().block();
//            log.info("Enrollment seeder has been successfully completed");
//        } catch (Exception error) {
//            log.error("Enrollment seeder failed during execution", error);
//            throw error;
//        }

    }

    public Mono<Void> createEnrollments(){
        return getCourses()
                .flatMap(course -> getStudents()
                        .flatMap(student-> enrollmentService.addOne(
                                Enrollment.builder()
                                        .courseId(course.getCourseId())
                                        .studentAcademicMember(student.getAcademicMemberId())
                                        .build())
                                .doOnSuccess(success->
                                        log.info("Success add Student:{} to Course:{} ",
                                                student.getFirstName(), course.getName()))
                                .doOnError(error->
                                        log.error("Error Add Student:{} to Course:{} ",
                                                student.getFirstName(), course.getName()))
                                .onErrorResume(error -> Mono.empty()))).then();
    }

    private Flux<Course> getCourses(){
        return courseService.findActive();
    }

    private Flux<AcademicMember> getStudents(){
        return Mono.zip(getCurrentAcademicYear(),getStudentRole()).flatMapMany(tuple ->{
            AcademicYear academicYear =tuple.getT1();
            Role studentRole = tuple.getT2();
            return  academicMemberService.findByAcademicYearId(
                    academicYear.getAcademicYearId(),studentRole.getRoleId()
            );
        });
    }

    private Mono<AcademicYear> getCurrentAcademicYear() {
        return academicYearService.getLatestAcademicYear()
                .switchIfEmpty(Mono.error(
                        new RuntimeException("AcademicYear not found")
                ))
                .doOnSuccess(academicYear ->
                        log.info("✓ AcademicYear loaded successfully")
                );
    }

    private Mono<Role> getStudentRole(){
        return roleService.findByName(RoleData.STUDENT.name())
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Student Role")))
                .doOnSuccess(role -> log.info("Found Student Role "))
                ;
    }
}
