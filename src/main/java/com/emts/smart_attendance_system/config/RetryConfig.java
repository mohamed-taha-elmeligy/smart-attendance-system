package com.emts.smart_attendance_system.config;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.validation.annotation.Validated;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * *******************************************************************
 * File: RetryConfig.java
 * Package: com.emts.smart_attendance_system.config
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Configuration
@ConfigurationProperties(prefix = "self.healing.retry")
@Slf4j
@Validated
@Getter
@Setter
public class RetryConfig {
    @Min(1)
    @Max(10)
    private int maxAttempts;

    @Positive
    private long initialDelay;

    @Positive
    private long maxDelay;

    @DecimalMax("1.0")
    @DecimalMin("0.1")
    private double jitter;


    public Retry createRetrySpec(String operationName){
        return Retry.backoff(maxAttempts, Duration.ofMillis(initialDelay))
                .maxBackoff(Duration.ofMillis(maxDelay))
                .jitter(jitter)
                .filter(this::isRetryable)
                .doBeforeRetry(signal ->
                        log.warn("[RETRY] {} attempt {}/{} due to: {}",
                                operationName, signal.totalRetries() + 1, maxAttempts, signal.failure().getMessage()
                        )
                )
                .onRetryExhaustedThrow((spec, signal) ->
                        new RuntimeException(
                                String.format("Failed %s after %d attempts",
                                        operationName, maxAttempts),
                                signal.failure()
                        )
                );
    }


    private boolean isRetryable(Throwable throwable) {
        return throwable instanceof TransientDataAccessException
                || throwable instanceof io.r2dbc.spi.R2dbcTransientResourceException
                || throwable instanceof java.net.ConnectException
                || throwable instanceof java.util.concurrent.TimeoutException
                || (throwable instanceof DataAccessException
                && throwable.getMessage() != null
                && throwable.getMessage().contains("connection"));
    }
}
