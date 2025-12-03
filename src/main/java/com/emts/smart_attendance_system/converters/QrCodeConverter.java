package com.emts.smart_attendance_system.converters;

import com.emts.smart_attendance_system.dtos.mappers.QrCodeMapper;
import com.emts.smart_attendance_system.dtos.requests.RequestQrCode;
import com.emts.smart_attendance_system.dtos.responses.ResponseQrCode;
import com.emts.smart_attendance_system.services.QrCodeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * *******************************************************************
 * File: QrCodeConverter.java
 * Package: com.emts.smart_attendance_system.converters
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 02/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@AllArgsConstructor
@Component
public class QrCodeConverter {
    private QrCodeService qrCodeService;
    private QrCodeMapper qrCodeMapper;

    public Mono<ResponseQrCode> generateQrCode(RequestQrCode requestQrCode) {
        return qrCodeMapper.toResponseMono(
                qrCodeService.generateQrCode(
                        requestQrCode.getNetworkInfo(),
                        requestQrCode.getDurationSeconds(),
                        requestQrCode.getLectureId()
                )
        );
    }

    public Mono<ResponseQrCode> findByIdActive(UUID qrCodeId) {
        return qrCodeMapper.toResponseMono(qrCodeService.findByIdActive(qrCodeId));
    }

    public Flux<ResponseQrCode> findByLectureId(UUID lectureId) {
        return qrCodeMapper.toResponseFlux(qrCodeService.findByLectureId(lectureId));
    }

    public Flux<ResponseQrCode> findByLectureIdAll(UUID lectureId) {
        return qrCodeMapper.toResponseFlux(qrCodeService.findByLectureIdAll(lectureId));
    }

    public Flux<ResponseQrCode> findExpiredByLectureId(UUID lectureId) {
        return qrCodeMapper.toResponseFlux(qrCodeService.findExpiredByLectureId(lectureId));
    }

    public Mono<Integer> expireQrCode(UUID qrCodeId) {
        return qrCodeService.expireQrCode(qrCodeId);
    }

    public Mono<Integer> inactivateQrCode(UUID qrCodeId) {
        return qrCodeService.inactivateQrCode(qrCodeId);
    }

    public Mono<Integer> activateQrCode(UUID qrCodeId) {
        return qrCodeService.activateQrCode(qrCodeId);
    }

    public Mono<Integer> expireByLectureId(UUID lectureId) {
        return qrCodeService.expireByLectureId(lectureId);
    }

    public Mono<Long> countByLectureId(UUID lectureId) {
        return qrCodeService.countByLectureId(lectureId);
    }
}

