package com.emts.smart_attendance_system.config.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.concurrent.atomic.AtomicBoolean;

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

@ReadingConverter
public class BooleanReadingConverter implements Converter<Boolean,AtomicBoolean> {

    @Override
    public AtomicBoolean convert(Boolean source) {
        return new AtomicBoolean(source);
    }
}
