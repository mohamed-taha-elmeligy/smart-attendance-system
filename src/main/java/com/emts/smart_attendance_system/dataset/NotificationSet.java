package com.emts.smart_attendance_system.dataset;

import com.emts.smart_attendance_system.entities.Notification;
import com.emts.smart_attendance_system.enums.TypeNotification;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.UUID;

/**
 * *******************************************************************
 * File: NotificationSet.java
 * Package: com.emts.smart_attendance_system.dataset
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 06/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
public class NotificationSet {

    UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public Flux<Notification> notificationSet(){
        return Flux.just(
                Notification.builder()
                        .message("أهلا وسهلا بك في نظام المراقبة الذكي للحضور! نتمنى لك تجربة رائعة")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("شكراً لتسجيلك! ابدأ الآن باستكشاف جميع ميزات النظام")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build()
        );
    }
}
