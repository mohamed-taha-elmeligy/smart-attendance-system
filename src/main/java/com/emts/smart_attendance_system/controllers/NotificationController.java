package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.NotificationConverter;
import com.emts.smart_attendance_system.dtos.responses.ResponseNotification;
import com.emts.smart_attendance_system.enums.TypeNotification;
import com.emts.smart_attendance_system.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: NotificationController.java
 * Package: com.emts.smart_attendance_system.controllers
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 20/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@RestController
@RequestMapping("/api/v1/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {
    private NotificationConverter notificationConverter;

    // Helper method to get current user's academicMemberId
    private Mono<UUID> getCurrentUserAcademicMemberId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(CustomUserDetails.class)
                .map(CustomUserDetails::getAcademicMemberId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated")));
    }

    // ===== Find by Current User =====
    @GetMapping("/my-notifications")
    public Flux<ResponseNotification> findMyNotifications() {
        log.debug("Fetching all notifications for current user");
        return getCurrentUserAcademicMemberId()
                .flatMapMany(academicMemberId -> {
                    log.debug("Fetching notifications for academicMemberId: {}", academicMemberId);
                    return notificationConverter.findByAcademicMemberId(academicMemberId)
                            .onErrorResume(throwable -> {
                                log.error("Error fetching notifications: {}", throwable.getMessage());
                                return Flux.empty();
                            });
                });
    }

    @GetMapping("/my-unread-notifications")
    public Flux<ResponseNotification> findMyUnreadNotifications() {
        log.debug("Fetching unread notifications for current user");
        return getCurrentUserAcademicMemberId()
                .flatMapMany(academicMemberId -> {
                    log.debug("Fetching unread notifications for academicMemberId: {}", academicMemberId);
                    return notificationConverter.findUnreadByAcademicMemberId(academicMemberId)
                            .onErrorResume(throwable -> {
                                log.error("Error fetching unread notifications: {}", throwable.getMessage());
                                return Flux.empty();
                            });
                });
    }

    @GetMapping("/my-read-notifications")
    public Flux<ResponseNotification> findMyReadNotifications() {
        log.debug("Fetching read notifications for current user");
        return getCurrentUserAcademicMemberId()
                .flatMapMany(academicMemberId -> {
                    log.debug("Fetching read notifications for academicMemberId: {}", academicMemberId);
                    return notificationConverter.findReadByAcademicMemberId(academicMemberId)
                            .onErrorResume(throwable -> {
                                log.error("Error fetching read notifications: {}", throwable.getMessage());
                                return Flux.empty();
                            });
                });
    }

    // ===== Find by Current User and Type =====
    @GetMapping("/my-unread-notifications-by-type")
    public Flux<ResponseNotification> findMyUnreadNotificationsByType(
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Fetching unread {} notifications for current user", type);
        return getCurrentUserAcademicMemberId()
                .flatMapMany(academicMemberId -> {
                    log.debug("Fetching unread {} notifications for academicMemberId: {}", type, academicMemberId);
                    return notificationConverter.findUnreadByAcademicMemberIdAndType(academicMemberId, type)
                            .onErrorResume(throwable -> {
                                log.error("Error fetching unread notifications by type: {}", throwable.getMessage());
                                return Flux.empty();
                            });
                });
    }

    @GetMapping("/my-read-notifications-by-type")
    public Flux<ResponseNotification> findMyReadNotificationsByType(
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Fetching read {} notifications for current user", type);
        return getCurrentUserAcademicMemberId()
                .flatMapMany(academicMemberId -> {
                    log.debug("Fetching read {} notifications for academicMemberId: {}", type, academicMemberId);
                    return notificationConverter.findReadByAcademicMemberIdAndType(academicMemberId, type)
                            .onErrorResume(throwable -> {
                                log.error("Error fetching read notifications by type: {}", throwable.getMessage());
                                return Flux.empty();
                            });
                });
    }

    // ===== Count =====
    @GetMapping("/my-unread-count")
    public Mono<ResponseEntity<Long>> countMyUnreadNotifications() {
        log.debug("Counting unread notifications for current user");
        return getCurrentUserAcademicMemberId()
                .flatMap(academicMemberId -> notificationConverter.countUnreadByAcademicMemberId(academicMemberId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting unread notifications: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/my-read-count")
    public Mono<ResponseEntity<Long>> countMyReadNotifications() {
        log.debug("Counting read notifications for current user");
        return getCurrentUserAcademicMemberId()
                .flatMap(academicMemberId -> notificationConverter.countReadByAcademicMemberId(academicMemberId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting read notifications: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/my-unread-count-by-type")
    public Mono<ResponseEntity<Long>> countMyUnreadNotificationsByType(
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Counting unread {} notifications for current user", type);
        return getCurrentUserAcademicMemberId()
                .flatMap(academicMemberId -> notificationConverter.countUnreadByAcademicMemberIdAndType(academicMemberId, type))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting unread notifications by type: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/my-read-count-by-type")
    public Mono<ResponseEntity<Long>> countMyReadNotificationsByType(
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Counting read {} notifications for current user", type);
        return getCurrentUserAcademicMemberId()
                .flatMap(academicMemberId -> notificationConverter.countReadByAcademicMemberIdAndType(academicMemberId, type))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting read notifications by type: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    // ===== Mark as Read =====
    @PutMapping("/mark-as-read")
    public Mono<ResponseEntity<ResponseNotification>> markAsRead(
            @RequestParam @NonNull UUID notificationId) {
        log.debug("Marking notification as read: {}", notificationId);
        return notificationConverter.markAsRead(notificationId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().body(null)))
                .onErrorResume(throwable -> {
                    log.error("Error marking notification as read: {}", throwable.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @PutMapping("/mark-all-as-read")
    public Flux<ResponseNotification> markAllAsRead() {
        log.debug("Marking all notifications as read for current user");
        return getCurrentUserAcademicMemberId()
                .flatMapMany(academicMemberId -> {
                    log.debug("Marking all notifications as read for academicMemberId: {}", academicMemberId);
                    return notificationConverter.markAllAsRead(academicMemberId)
                            .onErrorResume(throwable -> {
                                log.error("Error marking all notifications as read: {}", throwable.getMessage());
                                return Flux.empty();
                            });
                });
    }

    @PutMapping("/mark-all-as-read-by-type")
    public Flux<ResponseNotification> markAllAsReadByType(
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Marking all {} notifications as read for current user", type);
        return getCurrentUserAcademicMemberId()
                .flatMapMany(academicMemberId -> {
                    log.debug("Marking all {} notifications as read for academicMemberId: {}", type, academicMemberId);
                    return notificationConverter.markAllAsReadByType(academicMemberId, type)
                            .onErrorResume(throwable -> {
                                log.error("Error marking notifications as read by type: {}", throwable.getMessage());
                                return Flux.empty();
                            });
                });
    }

    // ===== Delete =====
    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Boolean>> deleteNotification(
            @RequestParam @NonNull UUID notificationId) {
        log.debug("Deleting notification: {}", notificationId);
        return getCurrentUserAcademicMemberId()
                .flatMap(academicMemberId -> notificationConverter.deleteByAcademicMemberIdAndNotificationId(academicMemberId, notificationId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error deleting notification: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(false));
                });
    }
}