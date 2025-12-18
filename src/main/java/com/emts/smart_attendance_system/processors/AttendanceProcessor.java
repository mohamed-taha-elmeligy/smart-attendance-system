package com.emts.smart_attendance_system.processors;

import com.emts.smart_attendance_system.entities.Attendance;
import com.emts.smart_attendance_system.entities.Enrollment;
import com.emts.smart_attendance_system.entities.Lecture;
import com.emts.smart_attendance_system.services.AttendanceService;
import com.emts.smart_attendance_system.services.EnrollmentService;
import com.emts.smart_attendance_system.services.LectureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
@Order(20)
public class AttendanceProcessor implements CommandLineRunner {

    private final AttendanceService attendanceService;
    private final EnrollmentService enrollmentService;
    private final LectureService lectureService;

    @Scheduled(cron = "0 0 6 * * *")
    public void scheduleAttendanceCreation() {
        log.info("Start creating attendance records for daily lectures");
        createAttendance()
                .flatMap(attendanceService::addAttendances)
                .collectList()
                .subscribe(
                        saved -> log.info("Successfully saved {} attendance records", saved.size()),
                        error -> log.error("Error in attendance creation process", error),
                        () -> log.info("Attendance creation process completed")
                );
    }

    private Flux<Attendance> createAttendance() {
        return getLectureInDay()
                .flatMap(lecture -> {
                    log.debug("Processing lecture: {}", lecture.getLectureId());
                    return getEnrollment(lecture.getCourseId())
                            .map(enrollment -> buildAttendance(lecture, enrollment))
                            .onErrorResume(error -> {
                                log.error(" Error fetching enrollments for course {}: {}",
                                        lecture.getCourseId(), error.getMessage());
                                return Flux.empty();
                            });
                })
                .onErrorResume(error -> {
                    log.error("Error in attendance creation: {}", error.getMessage());
                    return Flux.empty();
                });
    }

    private Attendance buildAttendance(Lecture lecture, Enrollment enrollment) {
        Attendance attendance = new Attendance();
        attendance.setStudentAcademicMemberId(enrollment.getStudentAcademicMember());
        attendance.setLectureId(lecture.getLectureId());
        return attendance;
    }

    private Flux<Lecture> getLectureInDay() {
        return lectureService.findByLectureDate(LocalDate.now())
                .onErrorResume(error -> {
                    log.error("Error fetching lectures for date {}: {}",
                            LocalDate.now(), error.getMessage());
                    return Flux.empty();
                });
    }

    private Flux<Enrollment> getEnrollment(UUID courseId) {
        return enrollmentService.findByCourseId(courseId)
                .onErrorResume(error -> {
                    log.error("Error fetching enrollments for course {}: {}",
                            courseId, error.getMessage());
                    return Flux.empty();
                });
    }

    @Override
    public void run(String... args) throws Exception {
//        log.info("Start creating attendance records for daily lectures..");
//        createAttendance()
//                .flatMap(attendanceService::addAttendances)
//                .collectList()
//                .subscribe(
//                        saved -> log.info("Completed: created {} attendance records", saved.size()),
//                        error -> log.error("Error completing attendance creation", error),
//                        () -> log.info("Attendance initialization completed")
//                );
    }
}