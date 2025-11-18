package com.emts.smart_attendance_system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * *******************************************************************
 * File: SmartAttendanceSystemApplication.java
 * Package: com.emts.smart_attendance_system
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 10/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@SpringBootApplication
@EnableScheduling
@Slf4j
public class SmartAttendanceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartAttendanceSystemApplication.class, args);
	}

}
