package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.entities.AcademicMember;
import com.emts.smart_attendance_system.entities.AcademicYear;
import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.entities.University;
import com.emts.smart_attendance_system.enums.DeveloperData;
import com.emts.smart_attendance_system.enums.RoleData;
import com.emts.smart_attendance_system.enums.UniversitiesData;
import com.emts.smart_attendance_system.services.AcademicMemberService;
import com.emts.smart_attendance_system.services.AcademicYearService;
import com.emts.smart_attendance_system.services.RoleService;
import com.emts.smart_attendance_system.services.UniversityService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j(topic = "SeederLogger")
@Component
@Order(4)
public class DeveloperSeeder implements CommandLineRunner {

    private final AcademicMemberService academicMemberService;
    private final RoleService roleService;
    private final UniversityService universityService;
    private final AcademicYearService academicYearService ;
    private Role cachedDeveloperRole;
    private University cachedDeveloperUniversity;
    private AcademicYear cachedDeveloperAcademicYear;

    public DeveloperSeeder(AcademicMemberService academicMemberService, RoleService roleService, UniversityService universityService, AcademicYearService academicYearService) {
        this.academicMemberService = academicMemberService;
        this.roleService = roleService;
        this.universityService = universityService;
        this.academicYearService = academicYearService;
    }

    @Override
    public void run(String... args){
        log.info("Starting initial AcademicMember seeding...");
        seedAcademicMembers().block();
        log.info("✓ Initial seeding completed");
    }


    private Mono<Void> seedAcademicMembers() {
        return listDevelopers()
                .flatMap(developer -> academicMemberService
                        .existsByUsernameAndUniversityNumber(
                                developer.getUsername(),
                                developer.getUniversityNumber()
                        )
                        .flatMap(exists -> {
                            if (Boolean.FALSE.equals(exists)) {
                                log.warn("Missing AcademicMember detected: {} - Creating...",
                                        developer.getUsername());
                                return createAcademicMember(developer);
                            }
                            return Mono.empty();
                        })
                        .onErrorResume(error -> {
                            log.error("✗ Error processing developer {}: {}",
                                    developer.getUsername(), error.getMessage());
                            return Mono.empty();
                        })
                )
                .then();
    }

    private Mono<AcademicMember> createAcademicMember(DeveloperData developer) {
        return Mono.zip(
                getCurrentAcademicYear(),
                getDeveloperUniversity(),
                getDeveloperRole()
        ).flatMap(tuple -> {
            AcademicYear academicYear = tuple.getT1();
            University university = tuple.getT2();
            Role role = tuple.getT3();

            String deviceId = generateDeviceId(developer.getUsername());

            AcademicMember member = AcademicMember.builder()
                    .firstName(developer.getFirstName())
                    .lastName(developer.getLastName())
                    .username(developer.getUsername())
                    .passwordHash(developer.getPhone())
                    .phone(developer.getPhone())
                    .email(developer.getEmail())
                    .birthdate(developer.getBirthdate())
                    .universityNumber(developer.getUniversityNumber())
                    .academicYearId(academicYear.getAcademicYearId())
                    .deviceId(deviceId)
                    .universityId(university.getUniversitiesId())
                    .roleId(role.getRoleId())
                    .build();

            return academicMemberService.addOne(member)
                    .doOnSuccess(m -> log.info("✓ AcademicMember created: {}", m.getUsername()))
                    .doOnError(e -> log.error("✗ Error creating AcademicMember {}: {}",
                            developer.getUsername(), e.getMessage()));
        });
    }


    private Flux<DeveloperData> listDevelopers() {
        return Flux.fromArray(DeveloperData.values());
    }

    private String generateDeviceId(String username) {
        return "DEV-" + username.toUpperCase();
    }

    private Mono<AcademicYear> getCurrentAcademicYear() {
        if (cachedDeveloperAcademicYear != null) {
            return Mono.just(cachedDeveloperAcademicYear);
        }
        return academicYearService.getLatestAcademicYear()
                .switchIfEmpty(Mono.error(
                        new RuntimeException("AcademicYear not found")
                ))
                .doOnSuccess(academicYear -> {
                    cachedDeveloperAcademicYear = academicYear;
                    log.info("✓ Developer AcademicYear loaded successfully");
                });
    }

    private Mono<University> getDeveloperUniversity() {
        if (cachedDeveloperUniversity != null) {
            return Mono.just(cachedDeveloperUniversity);
        }

        return universityService.findByName(UniversitiesData.DEVELOPERS_UNIVERSITY.name())
                .switchIfEmpty(Mono.error(
                        new RuntimeException("University not found: " + UniversitiesData.DEVELOPERS_UNIVERSITY.name())
                ))
                .doOnSuccess(university -> {
                    cachedDeveloperUniversity = university;
                    log.info("✓ Developer University loaded successfully");
                });
    }
    private Mono<Role> getDeveloperRole() {
        if (cachedDeveloperRole != null) {
            return Mono.just(cachedDeveloperRole);
        }

        return roleService.findByName(RoleData.DEVELOPER.name())
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Role not found: " + RoleData.DEVELOPER.name())
                ))
                .doOnSuccess(role -> {
                    cachedDeveloperRole = role;
                    log.info("✓ Developer role loaded successfully");
                });
    }
}