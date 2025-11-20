package com.emts.smart_attendance_system.controllers;

import com.emts.smart_attendance_system.converters.NotificationConverter;
import com.emts.smart_attendance_system.dtos.responses.ResponseNotification;
import com.emts.smart_attendance_system.enums.TypeNotification;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // ===== Find by Academic Member =====
    @GetMapping("/find-by-member")
    public Flux<ResponseNotification> findByAcademicMemberId(
            @RequestParam @NonNull UUID academicMemberId) {
        log.debug("Fetching all notifications for member: {}", academicMemberId);
        return notificationConverter.findByAcademicMemberId(academicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching notifications for member: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-unread-by-member")
    public Flux<ResponseNotification> findUnreadByAcademicMemberId(
            @RequestParam @NonNull UUID academicMemberId) {
        log.debug("Fetching unread notifications for member: {}", academicMemberId);
        return notificationConverter.findUnreadByAcademicMemberId(academicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching unread notifications for member: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-read-by-member")
    public Flux<ResponseNotification> findReadByAcademicMemberId(
            @RequestParam @NonNull UUID academicMemberId) {
        log.debug("Fetching read notifications for member: {}", academicMemberId);
        return notificationConverter.findReadByAcademicMemberId(academicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error fetching read notifications for member: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    // ===== Find by Academic Member and Type =====
    @GetMapping("/find-unread-by-member-and-type")
    public Flux<ResponseNotification> findUnreadByAcademicMemberIdAndType(
            @RequestParam @NonNull UUID academicMemberId,
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Fetching unread {} notifications for member: {}", type, academicMemberId);
        return notificationConverter.findUnreadByAcademicMemberIdAndType(academicMemberId, type)
                .onErrorResume(throwable -> {
                    log.error("Error fetching unread notifications: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("/find-read-by-member-and-type")
    public Flux<ResponseNotification> findReadByAcademicMemberIdAndType(
            @RequestParam @NonNull UUID academicMemberId,
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Fetching read {} notifications for member: {}", type, academicMemberId);
        return notificationConverter.findReadByAcademicMemberIdAndType(academicMemberId, type)
                .onErrorResume(throwable -> {
                    log.error("Error fetching read notifications: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    // ===== Count =====
    @GetMapping("/count-unread-by-member")
    public Mono<ResponseEntity<Long>> countUnreadByAcademicMemberId(
            @RequestParam @NonNull UUID academicMemberId) {
        log.debug("Counting unread notifications for member: {}", academicMemberId);
        return notificationConverter.countUnreadByAcademicMemberId(academicMemberId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting unread notifications: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/count-read-by-member")
    public Mono<ResponseEntity<Long>> countReadByAcademicMemberId(
            @RequestParam @NonNull UUID academicMemberId) {
        log.debug("Counting read notifications for member: {}", academicMemberId);
        return notificationConverter.countReadByAcademicMemberId(academicMemberId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting read notifications: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/count-unread-by-member-and-type")
    public Mono<ResponseEntity<Long>> countUnreadByAcademicMemberIdAndType(
            @RequestParam @NonNull UUID academicMemberId,
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Counting unread {} notifications for member: {}", type, academicMemberId);
        return notificationConverter.countUnreadByAcademicMemberIdAndType(academicMemberId, type)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting unread notifications by type: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    @GetMapping("/count-read-by-member-and-type")
    public Mono<ResponseEntity<Long>> countReadByAcademicMemberIdAndType(
            @RequestParam @NonNull UUID academicMemberId,
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Counting read {} notifications for member: {}", type, academicMemberId);
        return notificationConverter.countReadByAcademicMemberIdAndType(academicMemberId, type)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(0L))
                .onErrorResume(e -> {
                    log.error("Error counting read notifications by type: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(0L));
                });
    }

    // ===== Mark as Read/Unread =====
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
    public Flux<ResponseNotification> markAllAsRead(
            @RequestParam @NonNull UUID academicMemberId) {
        log.debug("Marking all notifications as read for member: {}", academicMemberId);
        return notificationConverter.markAllAsRead(academicMemberId)
                .onErrorResume(throwable -> {
                    log.error("Error marking all notifications as read: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    @PutMapping("/mark-all-as-read-by-type")
    public Flux<ResponseNotification> markAllAsReadByType(
            @RequestParam @NonNull UUID academicMemberId,
            @RequestParam @NonNull TypeNotification type) {
        log.debug("Marking all {} notifications as read for member: {}", type, academicMemberId);
        return notificationConverter.markAllAsReadByType(academicMemberId, type)
                .onErrorResume(throwable -> {
                    log.error("Error marking notifications as read by type: {}", throwable.getMessage());
                    return Flux.empty();
                });
    }

    // ===== Delete =====
    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Boolean>> deleteByAcademicMemberIdAndNotificationId(
            @RequestParam @NonNull UUID academicMemberId,
            @RequestParam @NonNull UUID notificationId) {
        log.debug("Deleting notification: {} for member: {}", notificationId, academicMemberId);
        return notificationConverter.deleteByAcademicMemberIdAndNotificationId(academicMemberId, notificationId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(false))
                .onErrorResume(e -> {
                    log.error("Error deleting notification: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(false));
                });
    }
}