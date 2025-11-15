package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.entities.University;
import com.emts.smart_attendance_system.enums.UniversitiesData;
import com.emts.smart_attendance_system.services.UniversityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
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
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j(topic = "SeederLogger")
@Component
@Order(1)
public class UniversitySeeder implements CommandLineRunner {

    private final UniversityService universityService ;

    private boolean appStarted = false ;

    public UniversitySeeder(UniversityService universityService) {
        this.universityService = universityService;
    }

    @Override
    public void run(String... args) {
        log.info("Starting initial University seeding...");
        seedUniversities().block(); // ✅ block during startup
        appStarted = true;
        log.info("✓ Initial University seeding completed");
    }

    @Scheduled(fixedRateString = "${self.healing.seeder.interval:3600000}")
    public void scheduledSeedUniversities() {
        if (appStarted) {
            log.info("Running scheduled University check (self-healing)...");
            seedUniversities()
                    .subscribe(
                            null,
                            error -> log.error("✗ Scheduled University seeding failed: {}", error.getMessage()),
                            () -> log.debug("✓ Scheduled University seeding completed")
                    );
        }
    }

    private Mono<Void> seedUniversities() {
        return universitiesDataFlux()
                .flatMap(university ->
                        universityService.existsByName(university.name())
                                .flatMap(existUniversity -> {
                                    if (Boolean.FALSE.equals(existUniversity)) {
                                        log.warn("Missing University detected: {} - Creating...", university.name());
                                        return universityService.addOne(createDefaultUniversity(university));
                                    }
                                    return Mono.empty();
                                })
                                .onErrorResume(error -> {
                                    // Handle individual university errors without stopping the whole chain
                                    log.error("✗ Error processing university {}: {}",
                                            university.name(), error.getMessage());
                                    return Mono.empty();
                                })
                )
                .then()
                .doOnSuccess(v -> log.info("✓ University seeding check completed"))
                .doOnError(error -> log.error("✗ Critical error during University seeding: {}", error.getMessage()));
    }

    private University createDefaultUniversity(UniversitiesData universitiesData){
        return University.builder()
                .name(universitiesData.name())
                .location(universitiesData.getLocation())
                .build();
    }
    private Flux<UniversitiesData> universitiesDataFlux(){
        return Flux.fromArray(
                UniversitiesData.values()
        );
    }

}
