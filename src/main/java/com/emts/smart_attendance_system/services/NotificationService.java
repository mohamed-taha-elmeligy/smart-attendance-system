package com.emts.smart_attendance_system.services;

import com.emts.smart_attendance_system.config.RetryConfig;
import com.emts.smart_attendance_system.entities.Notification;
import com.emts.smart_attendance_system.enums.TypeNotification;
import com.emts.smart_attendance_system.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {
    private NotificationRepository notificationRepository;
    private RetryConfig retryConfig;

    // ===== Create =====
    public Mono<Notification> addOne(Notification notification) {
        log.info("Attempting to save Notification for member: {}", notification.getAcademicMemberId());
        return notificationRepository.save(notification)
                .retryWhen(retryConfig.createRetrySpec("Add Notification"))
                .doOnSuccess(saved -> log.info("Saved Notification ID: {}", saved.getNotificationId()));
    }

    // ===== Find by Academic Member =====
    public Flux<Notification> findByAcademicMemberId(UUID academicMemberId) {
        log.debug("Fetching all notifications for member: {}", academicMemberId);
        return notificationRepository.findByAcademicMemberId(academicMemberId);
    }

    public Flux<Notification> findUnreadByAcademicMemberId(UUID academicMemberId) {
        log.debug("Fetching unread notifications for member: {}", academicMemberId);
        return notificationRepository.findByAcademicMemberIdAndIsReadFalse(academicMemberId);
    }

    public Flux<Notification> findReadByAcademicMemberId(UUID academicMemberId) {
        log.debug("Fetching read notifications for member: {}", academicMemberId);
        return notificationRepository.findByAcademicMemberIdAndIsReadTrue(academicMemberId);
    }

    // ===== Find by Academic Member and Type =====
    public Flux<Notification> findUnreadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        log.debug("Fetching unread notifications for member: {} by type: {}", academicMemberId, type);
        return notificationRepository.findByAcademicMemberIdAndTypeAndIsReadFalse(academicMemberId, type);
    }

    public Flux<Notification> findReadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        log.debug("Fetching read notifications for member: {} by type: {}", academicMemberId, type);
        return notificationRepository.findByAcademicMemberIdAndTypeAndIsReadTrue(academicMemberId, type);
    }

    // ===== Count =====
    public Mono<Long> countUnreadByAcademicMemberId(UUID academicMemberId) {
        return notificationRepository.countByAcademicMemberIdAndIsReadFalse(academicMemberId)
                .doOnNext(count -> log.debug("Unread notifications for member {}: {}", academicMemberId, count));
    }

    public Mono<Long> countReadByAcademicMemberId(UUID academicMemberId) {
        return notificationRepository.countByAcademicMemberIdAndIsReadTrue(academicMemberId)
                .doOnNext(count -> log.debug("Read notifications for member {}: {}", academicMemberId, count));
    }

    public Mono<Long> countUnreadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        return notificationRepository.countByAcademicMemberIdAndTypeAndIsReadFalse(academicMemberId, type)
                .doOnNext(count -> log.debug("Unread {} notifications for member {}: {}", type, academicMemberId, count));
    }

    public Mono<Long> countReadByAcademicMemberIdAndType(UUID academicMemberId, TypeNotification type) {
        return notificationRepository.countByAcademicMemberIdAndTypeAndIsReadTrue(academicMemberId, type);
    }

    // ===== Mark as Read/Unread =====
    public Mono<Notification> markAsRead(UUID notificationId) {
        log.info("Marking notification as read: {}", notificationId);
        return notificationRepository.markAsRead(notificationId)
                .retryWhen(retryConfig.createRetrySpec("Mark as Read"))
                .doOnSuccess(updated -> log.info("Notification marked as read: {}", notificationId));
    }

    public Flux<Notification> markAllAsRead(UUID academicMemberId) {
        log.info("Marking all notifications as read for member: {}", academicMemberId);
        return notificationRepository.markAllAsRead(academicMemberId)
                .retryWhen(retryConfig.createRetrySpec("Mark All as Read"))
                .doOnComplete(() -> log.info("All notifications marked as read for member: {}", academicMemberId));
    }

    public Flux<Notification> markAllAsReadByType(UUID academicMemberId, TypeNotification type) {
        log.info("Marking all {} notifications as read for member: {}", type, academicMemberId);
        return notificationRepository.markAllAsReadByType(academicMemberId, type)
                .retryWhen(retryConfig.createRetrySpec("Mark All as Read by Type"))
                .doOnComplete(() -> log.info("All {} notifications marked as read for member: {}", type, academicMemberId));
    }

    public Mono<Boolean> deleteByAcademicMemberIdAndNotificationId(UUID academicMemberId, UUID notificationId) {
        log.info("Deleting all read notifications for member: {}", academicMemberId);
        return notificationRepository.deleteByAcademicMemberIdAndNotificationId(academicMemberId,notificationId)
                .retryWhen(retryConfig.createRetrySpec("Delete Read Notifications"))
                .doOnSuccess(v -> log.info("All read notifications deleted for member: {}", academicMemberId));
    }
}