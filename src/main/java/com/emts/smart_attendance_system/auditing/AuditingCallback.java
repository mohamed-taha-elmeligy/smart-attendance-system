package com.emts.smart_attendance_system.auditing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.callback
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
@Slf4j
public class AuditingCallback implements BeforeConvertCallback<AuditableEntity> {

    @Override
    public Mono<AuditableEntity> onBeforeConvert(AuditableEntity entity, SqlIdentifier table) {
        Instant now = Instant.now();
        if (entity.getCreatedAt() == null)
            entity.setCreatedAt(now);

        entity.setUpdatedAt(now);
        log.debug("Updating audit timestamps for table [{}]", table.getReference());
        return Mono.just(entity);
    }
}
