package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.enums.RoleData;
import com.emts.smart_attendance_system.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * *******************************************************************
 * File: RoleSeeder.java
 * Package: com.emts.smart_attendance_system.seeder
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 30/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j(topic = "SeederLogger")
@Component
@Order(3)
public class RoleSeeder implements CommandLineRunner {

    private final RoleService roleService;
    private boolean appStarted = false ;

    public RoleSeeder(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args)  {
        log.info("Starting initial role seeding...");
        seedRoles().block(); // ✅ block during startup
        appStarted = true;
        log.info("✓ Initial role seeding completed");
    }

    @Scheduled(fixedRateString = "${self.healing.seeder.interval:3600000}")
    public void scheduledSeedRoles() {
        if (appStarted) {
            log.info("Running scheduled role check (self-healing)...");
            seedRoles()
                    .subscribe(
                            null,
                            error -> log.error("✗ Scheduled role seeding failed: {}", error.getMessage()),
                            () -> log.debug("✓ Scheduled role seeding completed")
                    );
        }
    }

    private Mono<Void> seedRoles() {
        return roleNamesFlux()
                .flatMap(roleName ->
                        roleService.existsByName(roleName.name())
                                .flatMap(existRole -> {
                                    if (Boolean.FALSE.equals(existRole)) {
                                        log.warn("Missing role detected: {} - Creating...", roleName);
                                        return roleService.addOne(createDefaultRole(roleName));
                                    }
                                    return Mono.empty();
                                })
                                .onErrorResume(error -> {
                                    // Handle individual role errors without stopping the whole chain
                                    log.error("✗ Error processing role {}: {}",
                                            roleName.name(), error.getMessage());
                                    return Mono.empty();
                                })
                )
                .then()
                .doOnSuccess(v -> log.info("✓ Role seeding check completed"))
                .doOnError(error -> log.error("✗ Critical error during role seeding: {}", error.getMessage()));
    }

    private Role createDefaultRole(RoleData roleName){
        return Role.builder()
                .name(roleName.name())
                .description(roleName.getDescription())
                .build();
    }

    private Flux<RoleData> roleNamesFlux(){
        return Flux.fromArray(
                RoleData.values()
        );
    }
}
