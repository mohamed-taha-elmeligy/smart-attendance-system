package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.dataset.LectureSet;
import com.emts.smart_attendance_system.entities.Course;
import com.emts.smart_attendance_system.services.CourseService;
import com.emts.smart_attendance_system.services.LectureService;
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
 * File: LectureSetSeeder.java
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
@Order(7)
public class LectureSetSeeder implements CommandLineRunner {

    private final LectureService lectureService;
    private final LectureSet lectureSet;
    private final CourseService courseService;


    public LectureSetSeeder(LectureService lectureService,
                            LectureSet lectureSet,
                            CourseService courseService) {
        this.lectureService = lectureService;
        this.lectureSet = lectureSet;
        this.courseService = courseService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting initial LectureSet seeding...");
//        createLecturesByDay().block();
        log.info("✓ Initial seeding completed");
    }

    private Mono<Void> createLecturesByDay() {
        return getCourses()
                .flatMapMany(courses -> {
                    if (courses.isEmpty()) {
                        log.warn("⚠ No courses found, cannot assign to lectures");
                        return Flux.empty();
                    }

                    log.info("Found {} courses for lecture assignment", courses.size());

                    // Distribute courses to lectures in round-robin fashion
                    AtomicInteger courseIndex = new AtomicInteger(0);

                    return lectureSet.lectureSet()
                            .doOnNext(lecture -> {
                                // Assign course in round-robin
                                Course course = courses.get(
                                        courseIndex.getAndIncrement() % courses.size()
                                );
                                lecture.setCourseId(course.getCourseId());
                                lecture.setInstructorAcademicMemberId(course.getInstructorAcademicMemberId());

                                log.debug("Assigning course {} and instructor {} to lecture {}",
                                        course.getCode(),
                                        course.getInstructorAcademicMemberId(),
                                        lecture.getRoom());
                            });
                })
                .flatMap(lecture ->
                        lectureService.addOne(lecture)
                                .doOnSuccess(success ->
                                        log.info("✓ Added lecture for {} at {} in room {} | Course: {} | Instructor: {}",
                                                success.getDayOfWeek(),
                                                success.getStartTime(),
                                                success.getRoom(),
                                                success.getCourseId(),
                                                success.getInstructorAcademicMemberId())
                                )
                                .doOnError(e ->
                                        log.error("✗ Failed to add lecture for room: {}",
                                                lecture.getRoom(), e)
                                )
                )
                .onErrorResume(e -> {
                    log.error("✗ Error during lecture seeding", e);
                    return Mono.empty();
                })
                .then();
    }

    /**
     * Get all active courses with instructor information
     */
    private Mono<List<Course>> getCourses() {
        return courseService.findActive()
                .collectList()
                .doOnSuccess(courses ->
                        log.info("✓ Loaded {} active courses", courses.size())
                )
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No active courses found")
                ));
    }
}