package com.emts.smart_attendance_system.dataset;

import com.emts.smart_attendance_system.entities.Lecture;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * *******************************************************************
 * File: LectureSet.java
 * Package: com.emts.smart_attendance_system.dataset
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 06/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
public class LectureSet {

    public Flux<Lecture> lectureSet(){
        return Flux.just(
                // Week 1 - Saturday (Jan 4, 2025)
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 4))
                        .dayOfWeek(DayOfWeek.SATURDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .room("A101")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 4))
                        .dayOfWeek(DayOfWeek.SATURDAY)
                        .startTime(LocalTime.of(10, 30))
                        .endTime(LocalTime.of(12, 30))
                        .room("A102")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 4))
                        .dayOfWeek(DayOfWeek.SATURDAY)
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(16, 0))
                        .room("A103")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 4))
                        .dayOfWeek(DayOfWeek.SATURDAY)
                        .startTime(LocalTime.of(16, 30))
                        .endTime(LocalTime.of(18, 30))
                        .room("A104")
                        .build(),
                // Week 1 - Sunday (Jan 5, 2025)
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 5))
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .room("B101")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 5))
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .startTime(LocalTime.of(10, 30))
                        .endTime(LocalTime.of(12, 30))
                        .room("B102")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 5))
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(16, 0))
                        .room("B103")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 5))
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .startTime(LocalTime.of(16, 30))
                        .endTime(LocalTime.of(18, 30))
                        .room("B104")
                        .build(),
                // Week 1 - Monday (Jan 6, 2025)
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 6))
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .room("C101")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 6))
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(10, 30))
                        .endTime(LocalTime.of(12, 30))
                        .room("C102")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 6))
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(16, 0))
                        .room("C103")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 6))
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(16, 30))
                        .endTime(LocalTime.of(18, 30))
                        .room("C104")
                        .build(),
                // Week 1 - Tuesday (Jan 7, 2025)
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 7))
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .room("D101")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 7))
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .startTime(LocalTime.of(10, 30))
                        .endTime(LocalTime.of(12, 30))
                        .room("D102")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 7))
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(16, 0))
                        .room("D103")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 7))
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .startTime(LocalTime.of(16, 30))
                        .endTime(LocalTime.of(18, 30))
                        .room("D104")
                        .build(),
                // Week 1 - Wednesday (Jan 8, 2025)
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 8))
                        .dayOfWeek(DayOfWeek.WEDNESDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .room("E101")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 8))
                        .dayOfWeek(DayOfWeek.WEDNESDAY)
                        .startTime(LocalTime.of(10, 30))
                        .endTime(LocalTime.of(12, 30))
                        .room("E102")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 8))
                        .dayOfWeek(DayOfWeek.WEDNESDAY)
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(16, 0))
                        .room("E103")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 8))
                        .dayOfWeek(DayOfWeek.WEDNESDAY)
                        .startTime(LocalTime.of(16, 30))
                        .endTime(LocalTime.of(18, 30))
                        .room("E104")
                        .build(),
                // Week 1 - Thursday (Jan 9, 2025)
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 9))
                        .dayOfWeek(DayOfWeek.THURSDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .room("F101")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 9))
                        .dayOfWeek(DayOfWeek.THURSDAY)
                        .startTime(LocalTime.of(10, 30))
                        .endTime(LocalTime.of(12, 30))
                        .room("F102")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 9))
                        .dayOfWeek(DayOfWeek.THURSDAY)
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(16, 0))
                        .room("F103")
                        .build(),
                Lecture.builder()
                        .lectureDate(LocalDate.of(2025, 1, 9))
                        .dayOfWeek(DayOfWeek.THURSDAY)
                        .startTime(LocalTime.of(16, 30))
                        .endTime(LocalTime.of(18, 30))
                        .room("F104")
                        .build()
        );
    }
}