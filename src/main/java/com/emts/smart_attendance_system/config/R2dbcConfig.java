package com.emts.smart_attendance_system.config;

import com.emts.smart_attendance_system.config.converters.BooleanReadingConverter;
import com.emts.smart_attendance_system.config.converters.BooleanWritingConverter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.List;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.config.converters
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
@Configuration
@AllArgsConstructor
@EnableR2dbcRepositories(basePackages = "com.emts.smart_attendance_system.repositories")
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory;

    @Override
    public ConnectionFactory connectionFactory() {
        return this.connectionFactory;
    }
    @Override
    protected List<Object> getCustomConverters() {
        return List.of(
                new BooleanReadingConverter(),
                new BooleanWritingConverter()
        );
    }

}

