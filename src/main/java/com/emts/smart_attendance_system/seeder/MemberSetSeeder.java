package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.dataset.MemberSet;
import com.emts.smart_attendance_system.entities.AcademicMember;
import com.emts.smart_attendance_system.entities.AcademicYear;
import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.entities.University;
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

import java.util.Map;
import java.util.function.Supplier;

/**
 * *******************************************************************
 * File: MemberSetSeeder.java
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
@Order(5)
public class MemberSetSeeder implements CommandLineRunner {

    private final AcademicMemberService academicMemberService;
    private final RoleService roleService;
    private final UniversityService universityService;
    private final AcademicYearService academicYearService;
    private final MemberSet memberSet;

    private final Map<RoleData, Supplier<Flux<AcademicMember>>> ROLE_MEMBER_MAP;

    public MemberSetSeeder(AcademicMemberService academicMemberService,
                           RoleService roleService,
                           UniversityService universityService,
                           AcademicYearService academicYearService,
                           MemberSet memberSet) {
        this.academicMemberService = academicMemberService;
        this.roleService = roleService;
        this.universityService = universityService;
        this.academicYearService = academicYearService;
        this.memberSet = memberSet;

        this.ROLE_MEMBER_MAP = Map.of(
                RoleData.STUDENT, memberSet::studentSet,
                RoleData.TEACHER, memberSet::teacherSet,
                RoleData.USER, memberSet::userSet,
                RoleData.ADMIN, memberSet::adminSet
        );
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting initial MemberSet seeding...");

//        for (RoleData roleData : ROLE_MEMBER_MAP.keySet()) {
//            createMemberByRole(roleData).block();
//        }

        log.info("✓ Initial seeding completed");
    }

    private Mono<Void> createMemberByRole(RoleData roleData) {
        Supplier<Flux<AcademicMember>> memberSupplier = ROLE_MEMBER_MAP.get(roleData);

        return Mono.zip(
                        getMembersUniversity(),
                        getCurrentAcademicYear(),
                        getMemberRole(roleData)
                )
                .flatMapMany(tuple -> {
                    University university = tuple.getT1();
                    AcademicYear academicYear = tuple.getT2();
                    Role role = tuple.getT3();

                    return memberSupplier.get()
                            .doOnNext(member -> {
                                member.setUniversityId(university.getUniversitiesId());
                                member.setAcademicYearId(academicYear.getAcademicYearId());
                                member.setRoleId(role.getRoleId());
                            });
                })
                .flatMap(member ->
                        academicMemberService.addOne(member)
                                .doOnSuccess(success -> {
                                    if (success != null) {
                                        log.info("✓ Successfully added member: {}" , success.getUsername());
                                    } else {
                                        log.error("✗ Member already exists:{} " , member.getUsername());
                                    }                                }  )
                                .doOnError(e -> {
                                            log.error("✗ Failed to add {} member: {}", roleData.name(), member.getUsername(), e);
                                        }
                                )
                )
                .onErrorResume(e -> {
                    log.error("✗ Error during {} seeding", roleData.name(), e);
                    return Mono.empty();
                })
                .then();
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

    private Mono<University> getMembersUniversity() {
        return universityService.findByName(UniversitiesData.HIET.name())
                .switchIfEmpty(Mono.error(
                        new RuntimeException("University not found: " + UniversitiesData.HIET.name())
                ))
                .doOnSuccess(university ->
                        log.info("✓ University loaded successfully")
                );
    }

    private Mono<Role> getMemberRole(RoleData roleData) {
        return roleService.findByName(roleData.name())
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Role not found: " + roleData.name())
                ))
                .doOnSuccess(role ->
                        log.info("✓ {} role loaded successfully", roleData.name())
                );
    }
}