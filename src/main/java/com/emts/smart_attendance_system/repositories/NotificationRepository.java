package com.emts.smart_attendance_system.repositories;

import com.emts.smart_attendance_system.entities.Notification;
import com.emts.smart_attendance_system.enums.TypeNotification;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NotificationRepository extends ReactiveCrudRepository<Notification, UUID> {

    // ===== Find by Academic Member =====
    Flux<Notification> findByAcademicMemberIdAndIsReadFalse(UUID academicMemberId);

    Flux<Notification> findByAcademicMemberIdAndIsReadTrue(UUID academicMemberId);

    Flux<Notification> findByAcademicMemberId(UUID academicMemberId);

    // ===== Find by Academic Member and Type =====
    Flux<Notification> findByAcademicMemberIdAndTypeAndIsReadFalse(UUID academicMemberId, TypeNotification type);

    Flux<Notification> findByAcademicMemberIdAndTypeAndIsReadTrue(UUID academicMemberId, TypeNotification type);

    // ===== Count =====
    Mono<Long> countByAcademicMemberIdAndIsReadFalse(UUID academicMemberId);

    Mono<Long> countByAcademicMemberIdAndIsReadTrue(UUID academicMemberId);

    Mono<Long> countByAcademicMemberIdAndTypeAndIsReadFalse(UUID academicMemberId, TypeNotification type);

    Mono<Long> countByAcademicMemberIdAndTypeAndIsReadTrue(UUID academicMemberId, TypeNotification type);

    // ===== Update Read Status =====
    @Query("UPDATE notification SET is_read = TRUE WHERE notification_id = :notificationId RETURNING *")
    Mono<Notification> markAsRead(UUID notificationId);

    @Query("UPDATE notification SET is_read = TRUE WHERE academic_member_id = :academicMemberId RETURNING *")
    Flux<Notification> markAllAsRead(UUID academicMemberId);

    @Query("UPDATE notification SET is_read = TRUE WHERE academic_member_id = :academicMemberId AND type = :type RETURNING *")
    Flux<Notification> markAllAsReadByType(UUID academicMemberId, TypeNotification type);

    @Query("DELETE FROM notification WHERE academic_member_id = :academicMemberId AND notification_id = :notificationId")
    Mono<Boolean> deleteByAcademicMemberIdAndNotificationId(UUID academicMemberId, UUID notificationId);
}