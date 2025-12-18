package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.entities.Lecture;
import com.emts.smart_attendance_system.services.LectureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * *******************************************************************
 * File: LectureSeeder.java
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
@Order(19)
public class LectureSeeder implements CommandLineRunner {

    private final LectureService lectureService;

    public LectureSeeder(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    /**
     * Runs every day at 00:00 (midnight) - 12 AM
     * Fetches lectures for the current day from database based on DayOfWeek
     */

    public void run (String... arg)  {
//        addLecturesForToday();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledLectureSeeding() {
        log.info("⏰ Starting scheduled LectureSet seeding...");
        addLecturesForToday();
    }

    private void addLecturesForToday() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        log.info("📅 Fetching lectures for: {}", today);

        lectureService.findByDayOfWeek(today)
                .flatMap(lecture ->
                        lectureService.addOne(createNewLecture(lecture))
                                .doOnSuccess(success -> log.info("✓ Lecture added"))
                                .switchIfEmpty(Mono.fromRunnable(() ->
                                        log.info("ℹ Lecture already exists for this date")
                                ))
                                .doOnError(e ->
                                        log.error("✗ Failed to add lecture for course: {}",
                                                lecture.getCourseId(), e)
                                )
                )
                .onErrorResume(e -> {
                    log.error("✗ Error during scheduled lecture seeding", e);
                    return Mono.empty();
                })
                .subscribe(
                        v -> {},
                        e -> log.error("✗ Subscription error in lecture seeding", e),
                        () -> log.info("✓ Scheduled seeding completed for {}", today)
                );
    }

    /**
     * Creates a new lecture instance from existing lecture template
     * Sets lectureId to null and updates lectureDate to today
     */
    private Lecture createNewLecture(Lecture lecture) {
        // Using reflection or builder pattern depending on your Lecture entity
        // If Lecture has a toBuilder() method:
        lecture.setLectureDate(LocalDate.now());
        lecture.setLectureId(null);
        return lecture;
    }

}