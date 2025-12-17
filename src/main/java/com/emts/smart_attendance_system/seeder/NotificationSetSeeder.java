package com.emts.smart_attendance_system.seeder;

import com.emts.smart_attendance_system.dataset.NotificationSet;
import com.emts.smart_attendance_system.entities.AcademicMember;
import com.emts.smart_attendance_system.entities.Notification;
import com.emts.smart_attendance_system.services.AcademicMemberService;
import com.emts.smart_attendance_system.services.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.seeder
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 17/12/2025
 * Port Number: 8083
 * *******************************************************************
 */
@Component
@AllArgsConstructor
@Slf4j
@Order(21)
public class NotificationSetSeeder implements CommandLineRunner {
    private final NotificationService notificationService;
    private final AcademicMemberService memberService;
    private final NotificationSet set;

    public void run (String... arg) throws Exception {
        log.info("Start Notification Seeder");
//        addNotification();
        log.info("Completed Notification Seeder");
    }

    private void addNotification(){
        getMembers()
                .flatMap(member -> createNotification(member.getAcademicMemberId()))
                .buffer(100)
                .doOnNext(batch -> log.info("Processed batch of {}", batch.size()))
                .blockLast();
    }

    private Flux<Notification> createNotification(UUID memberId){
        return set.notificationSet()
                .doOnNext(notification -> notification.setAcademicMemberId(memberId))
                .flatMap(notificationService::addOne);
    }

    private Flux<AcademicMember> getMembers(){
        return memberService.getAll();
    }
}
