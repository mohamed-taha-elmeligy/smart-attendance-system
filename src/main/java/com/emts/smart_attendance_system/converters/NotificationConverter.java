package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.NotificationMapper;
import com.emts.smart_attendance_system.dtos.responses.ResponseNotification;
import com.emts.smart_attendance_system.enums.TypeNotification;
import com.emts.smart_attendance_system.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 20/11/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@Component
public class NotificationConverter {
    private NotificationService notificationService;
    private NotificationMapper notificationMapper;

    // ===== Find by Academic Member =====
    public Flux<ResponseNotification> findByAcademicMemberId(UUID academicMemberId) {
        return notificationMapper.toResponseFlux(
                notificationService.findByAcademicMemberId(academicMemberId)
        );
    }

    public Flux<ResponseNotification> findUnreadByAcademicMemberId(UUID academicMemberId) {
        return notificationMapper.toResponseFlux(
                notificationService.findUnreadByAcademicMemberId(academicMemberId)
        );
    }

    public Flux<ResponseNotification> findReadByAcademicMemberId(UUID academicMemberId) {
        return notificationMapper.toResponseFlux(
                notificationService.findReadByAcademicMemberId(academicMemberId)
        );
    }

    // ===== Find by Academic Member and Type =====
    public Flux<ResponseNotification> findUnreadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        return notificationMapper.toResponseFlux(
                notificationService.findUnreadByAcademicMemberIdAndType(academicMemberId, type)
        );
    }

    public Flux<ResponseNotification> findReadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        return notificationMapper.toResponseFlux(
                notificationService.findReadByAcademicMemberIdAndType(academicMemberId, type)
        );
    }

    // ===== Count =====
    public Mono<Long> countUnreadByAcademicMemberId(UUID academicMemberId) {
        return notificationService.countUnreadByAcademicMemberId(academicMemberId);
    }

    public Mono<Long> countReadByAcademicMemberId(UUID academicMemberId) {
        return notificationService.countReadByAcademicMemberId(academicMemberId);
    }

    public Mono<Long> countUnreadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        return notificationService.countUnreadByAcademicMemberIdAndType(academicMemberId, type);
    }

    public Mono<Long> countReadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        return notificationService.countReadByAcademicMemberIdAndType(academicMemberId, type);
    }

    // ===== Mark as Read/Unread =====
    public Mono<ResponseNotification> markAsRead(UUID notificationId) {
        return notificationMapper.toResponseMono(
                notificationService.markAsRead(notificationId)
        );
    }

    public Flux<ResponseNotification> markAllAsRead(UUID academicMemberId) {
        return notificationMapper.toResponseFlux(
                notificationService.markAllAsRead(academicMemberId)
        );
    }


    public Flux<ResponseNotification> markAllAsReadByType(UUID academicMemberId, TypeNotification type) {
        return notificationMapper.toResponseFlux(
                notificationService.markAllAsReadByType(academicMemberId, type)
        );
    }

    public Mono<Boolean> deleteByAcademicMemberIdAndNotificationId(UUID academicMemberId, UUID notificationId) {
        return notificationService.deleteByAcademicMemberIdAndNotificationId(academicMemberId,notificationId);
    }
}
