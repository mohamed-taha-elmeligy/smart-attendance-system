package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.dataset.CourseSet;
import com.emts.smart_attendance_system.entities.AcademicMember;
import com.emts.smart_attendance_system.entities.AcademicYear;
import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.entities.University;
import com.emts.smart_attendance_system.enums.RoleData;
import com.emts.smart_attendance_system.enums.UniversitiesData;
import com.emts.smart_attendance_system.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * *******************************************************************
 * File: CourseSetSeeder.java
 * Package: com.emts.smart_attendance_system.seeder
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 06/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j(topic = "SeederLogger")
@Component
@Order(6)
public class CourseSetSeeder implements CommandLineRunner {

    private final UniversityService universityService;
    private final AcademicYearService academicYearService;
    private final CourseService courseService;
    private final CourseSet courseSet;
    private final AcademicMemberService academicMemberService;
    private final RoleService roleService;


    public CourseSetSeeder(UniversityService universityService,
                           AcademicYearService academicYearService,
                           CourseService courseService,
                           CourseSet courseSet,
                           AcademicMemberService academicMemberService,
                           RoleService roleService) {
        this.universityService = universityService;
        this.academicYearService = academicYearService;
        this.courseService = courseService;
        this.courseSet = courseSet;
        this.academicMemberService = academicMemberService;
        this.roleService = roleService;
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("Starting initial CourseSet seeding...");
//        createCourse().block();
        log.info("✓ Initial seeding completed");
    }

    private Mono<Void> createCourse() {
        return Mono.zip(getMembersUniversity(), getCurrentAcademicYear(), getTeachers())
                .flatMapMany(tuple -> {
                    University university = tuple.getT1();
                    AcademicYear academicYear = tuple.getT2();
                    List<AcademicMember> teachers = tuple.getT3();

                    if (teachers.isEmpty()) {
                        log.warn("⚠ No teachers found, cannot assign instructors to courses");
                        return Flux.empty();
                    }

                    // Distribute teachers across courses in round-robin fashion
                    AtomicInteger teacherIndex = new AtomicInteger(0);

                    return courseSet.courseSet()
                            .doOnNext(course -> {
                                course.setAcademicYearId(academicYear.getAcademicYearId());
                                course.setUniversityId(university.getUniversitiesId());

                                // Assign teacher in round-robin
                                AcademicMember teacher = teachers.get(
                                        teacherIndex.getAndIncrement() % teachers.size()
                                );
                                course.setInstructorAcademicMemberId(teacher.getAcademicMemberId());

                                log.debug("Assigning {} to course {}",
                                        teacher.getUsername(), course.getCode());
                            });
                })
                .flatMap(course ->
                        courseService.addOne(course)
                                .doOnSuccess(success ->
                                        log.info("✓ Added Course: {} with Instructor: {}",
                                                success.getCode(),
                                                success.getInstructorAcademicMemberId())
                                )
                                .doOnError(e ->
                                        log.error("✗ Failed to add course: {}", course.getCode(), e)
                                )
                )
                .onErrorResume(e -> {
                    log.error("✗ Error during course seeding", e);
                    return Mono.empty();
                })
                .then();
    }

    private Mono<University> getMembersUniversity() {
        return universityService.findByName(UniversitiesData.HIET.name())
                .switchIfEmpty(Mono.error(
                        new RuntimeException("University not found: " + UniversitiesData.HIET.name())
                ))
                .doOnSuccess(university ->
                        log.info("✓ University loaded successfully")
                );
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

    /**
     * Get all teachers (members with TEACHER role)
     */
    private Mono<List<AcademicMember>> getTeachers() {
        return getMemberRole()
                .flatMapMany(role ->
                        academicMemberService.findByRoleId(role.getRoleId())
                )
                .collectList()
                .doOnSuccess(teachers ->
                        log.info("✓ Loaded {} teachers", teachers.size())
                )
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No teachers found")
                ));
    }

    /**
     * Get TEACHER role
     */
    private Mono<Role> getMemberRole() {
        return roleService.findByName(RoleData.TEACHER.name())
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Role not found: " + RoleData.TEACHER.name())
                ))
                .doOnSuccess(role ->
                        log.info("✓ {} role loaded successfully", RoleData.TEACHER.name())
                );
    }
}