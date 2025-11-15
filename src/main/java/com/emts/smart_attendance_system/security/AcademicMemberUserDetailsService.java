package com.emts.smart_attendance_system.security;

import com.emts.smart_attendance_system.entities.AcademicMember;
import com.emts.smart_attendance_system.entities.Role;
import com.emts.smart_attendance_system.repositories.RoleRepository;
import com.emts.smart_attendance_system.services.AcademicMemberService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * *******************************************************************
 * File: CustomReactiveUserDetailsService.java
 * Package: com.emts.smart_attendance_system.security
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 28/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j
@Service
@AllArgsConstructor
public class AcademicMemberUserDetailsService implements ReactiveUserDetailsService {

    private final AcademicMemberService academicMemberService;
    private final RoleRepository roleRepository;
    private static final String ROLE = "ROLE_";
    private final Cache<UUID, Role> roleCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))
            .maximumSize(100)
            .build();



    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.debug("Loading academic member by username: {}", username);

        return academicMemberService.findByUsername(username)

                .switchIfEmpty(Mono.defer(()->{
                        log.warn("Academic member not found with username: {}", username);
                        return Mono.error(new UsernameNotFoundException("Academic member not found with username: "+ username));
                }))

                .flatMap(member ->{
                    return getRoleById(member.getRoleId()).

                            map(role -> {
                                Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(ROLE+role.getName()));
                                log.debug("Academic member {} has role: ROLE_{}", username, role.getName());
                                return (UserDetails) buildAcademicMember(member,role);
                            }).

                            switchIfEmpty(Mono.defer(()->{
                                log.error("Role not found for academic member: {}", username);
                                return Mono.error( new UsernameNotFoundException("Role not found for academic member: "+ username));
                            }));
                })

                .doOnNext(userDetails -> log.info("Successfully loaded academic member: {}", username))

                .doOnError(error -> {
                    if (!(error instanceof UsernameNotFoundException)) {
                        log.error("Error loading academic member {}: {}",
                                username, error.getMessage());
                    }}
                );

    }

    Mono<Role> getRoleById(UUID roleId) {
        Role cached = roleCache.getIfPresent(roleId);
        if (cached != null) return Mono.just(cached);
        return roleRepository.findById(roleId)
                .doOnNext(role -> roleCache.put(roleId, role));
    }

    private CustomUserDetails buildAcademicMember(AcademicMember member , Role role){
        return new CustomUserDetails(
                member.getAcademicMemberId(),
                member.getUsername(),
                member.getPasswordHash(),
                member.isValidEmail(),
                role.getName(),
                member.isSoftDelete(),
                member.getUniversityNumber(),
                member.getEmail(),
                member.getDeviceId()
        );
    }
}