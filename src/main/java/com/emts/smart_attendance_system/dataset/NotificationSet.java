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
                        .message("تم تفعيل حسابك بنجاح. يمكنك الآن البدء في استخدام النظام")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("الترحيب بك في eMTS! نحن هنا لمساعدتك على تتبع الحضور بسهولة")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("شكراً لتسجيلك! ابدأ الآن باستكشاف جميع ميزات النظام")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("حسابك جاهز الآن! تابع محاضراتك وعلامات الحضور من لوحة التحكم")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("مرحباً بك في عائلتنا! لا تتردد في التواصل معنا إذا احتجت لأي مساعدة")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("تم تفعيل ملفك الشخصي. استمتع بتجربة سلسة في تتبع الحضور")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("نرحب بك في منصتنا التعليمية الحديثة. استكشف الآن!")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("أهلا بك! تم تسجيلك بنجاح وحسابك جاهز للاستخدام الفوري")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("مرحباً! نحن سعداء بانضمامك إلى نظام المراقبة الذكي")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("تفعيل البريد الإلكتروني تم! يمكنك الآن الوصول لجميع الخدمات")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("أهلاً وسهلاً! حسابك تم إنشاؤه بنجاح وجاهز للاستخدام")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("مرحبا بك في eMTS Smart Attendance System! ابدأ رحلتك الآن")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("نرحب بك بحرارة! استكشف الميزات المتقدمة للنظام")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("الترحيب بك في النظام! نتمنى أن تستمتع بالتجربة")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("تم التحقق من حسابك بنجاح! أنت الآن عضو فعال في النظام")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("أهلا وسهلا! جميع الميزات متاحة الآن لك")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("الحمد لله على التسجيل الناجح! ابدأ الآن باستخدام النظام")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("مرحبا! تم إنشاء حسابك وتفعيله. استمتع بالخدمات")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build(),
                Notification.builder()
                        .message("نرحب بك بأطيب ترحيب! حسابك جاهز الآن للاستخدام الفوري")
                        .type(TypeNotification.WELCOME)
                        .academicMemberId(uuid)
                        .build()
        );
    }
}