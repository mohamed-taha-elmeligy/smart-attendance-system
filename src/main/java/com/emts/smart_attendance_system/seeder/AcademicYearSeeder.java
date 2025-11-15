package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.entities.AcademicYear;
import com.emts.smart_attendance_system.services.AcademicYearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Year;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * *******************************************************************
 * File: AcademicYearSeeder.java
 * Package: com.emts.smart_attendance_system.seeder
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j(topic = "SeederLogger")
@Component
@Order(2)
public class AcademicYearSeeder implements CommandLineRunner {

    private final AcademicYearService academicYearService;
    private static final Map<String, String> cacheAcademicYear = new ConcurrentHashMap<>();
    private boolean appStarted = false;

    public AcademicYearSeeder(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @Override
    public void run(String... args) {
        log.info("Starting AcademicYear seeder initialization...");

        try {
            loadCacheFromDatabase()
                    .then(Mono.defer(() -> seedAcademicYearReactive(createNewAcademicYear())))
                    .doOnSuccess(v ->
                            log.info("AcademicYear seeder initialized. Cache size: {}", cacheAcademicYear.size())
                    )
                    .block(); // Wait for initialization to complete
            log.info("Seeder ready for scheduled tasks");

        } catch (Exception e) {
            log.error("Failed to initialize seeder: {}", e.getMessage(), e);
        }
        appStarted = true;
    }

    // Load all academic years from DB into cache
    private Mono<Void> loadCacheFromDatabase() {
        return academicYearService.findAll()
                .doOnNext(academicYear -> {
                    cacheAcademicYear.put(
                            academicYear.getCode(),
                            academicYear.getDescription()
                    );
                    log.debug("Loaded into cache: {}", academicYear.getCode());
                })
                .then()
                .doOnSuccess(v -> log.info("Cache loaded with {} academic years", cacheAcademicYear.size()));
    }

    // Opens on September 1st every year at 00:00
    @Scheduled(cron = "0 0 0 1 9 *")
    public void scheduledCreateNewYear(){
        if (appStarted) {
            log.info("Creating new AcademicYear on September 1st...");
            seedAcademicYearReactive(createNewAcademicYear())
                    .subscribe();
        }
    }

    // Self-healing: checks every hour
    @Scheduled(fixedRateString = "${self.healing.seeder.interval:3600000}")
    public void scheduledSelfHealing(){
        if (appStarted && !cacheAcademicYear.isEmpty()) {
            log.info("Running self-healing check for {} academic years...", cacheAcademicYear.size());

            Flux.fromIterable(cacheAcademicYear.entrySet())
                    .flatMap(entry ->
                            seedAcademicYearReactive(AcademicYear.builder()
                                    .code(entry.getKey())
                                    .description(entry.getValue())
                                    .build())
                    )
                    .then()
                    .doOnSuccess(v -> log.info("Self-healing completed successfully"))
                    .doOnError(error -> log.error("Self-healing failed: {}", error.getMessage()))
                    .subscribe();
        }
    }

    private Mono<Void> seedAcademicYearReactive(AcademicYear academicYear){
        return academicYearService.existsByCode(academicYear.getCode())
                .defaultIfEmpty(false)
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)){
                        log.warn("AcademicYear {} not found in DB! Restoring...", academicYear.getCode());
                        return academicYearService.addOne(academicYear)
                                .doOnSuccess(saved -> {
                                    cacheAcademicYear.put(saved.getCode(), saved.getDescription());
                                    log.info("Restored AcademicYear: {}", saved.getCode());
                                });
                    }
                    return Mono.empty();
                })
                .then()
                .doOnError(error ->
                        log.error("Failed to seed AcademicYear {}: {}",
                                academicYear.getCode(), error.getMessage())
                );
    }

    private AcademicYear createNewAcademicYear(){
        Year nowYear = Year.now();
        int currentMonth = LocalDate.now().getMonthValue();
        int academicYearStart = (currentMonth < 9) ? nowYear.getValue() - 1 : nowYear.getValue();

        return AcademicYear.builder()
                .code("AY" + academicYearStart)
                .description(String.format("Academic Year %d/%d",
                        academicYearStart,
                        academicYearStart + 1))
                .build();
    }
}