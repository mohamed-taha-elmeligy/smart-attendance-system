package com.emts.smart_attendance_system.dataset;

import com.emts.smart_attendance_system.entities.AcademicMember;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * *******************************************************************
 * File: MemberSet.java
 * Package: com.emts.smart_attendance_system.dataset
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 06/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
public class MemberSet {

    private static final String ADMIN = "Admin";
    private static final String USER = "User";

    public Flux<AcademicMember> studentSet(){
        return Flux.just();
    }

    public Flux<AcademicMember> teacherSet(){
        return Flux.just();
    }

    public Flux<AcademicMember> adminSet(){
        return Flux.just();
    }

    public Flux<AcademicMember> userSet(){
        return Flux.just();
    }
}
