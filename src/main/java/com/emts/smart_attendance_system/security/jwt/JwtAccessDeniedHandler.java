package com.emts.smart_attendance_system.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * *******************************************************************
 * File: JwtAccessDeniedHandler.java
 * Package: com.emts.smart_attendance_system.security.jwt
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 10/10/2025
 * Port Number: 8083
 * *******************************************************************
 */
@Slf4j
@Component
@AllArgsConstructor
public class JwtAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private static final String ACCESS_DENIED = "Access Denied";


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("{}: {}",ACCESS_DENIED,denied.getMessage());

        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> errorResponse = createErrorResponse(
                denied.getMessage() != null ? denied.getMessage() : "You don't have permission to access this resource",
                exchange.getRequest().getPath().value()
        );

        return writeResponse(exchange,errorResponse);
    }

    private Map<String,Object> createErrorResponse(String message , String path){
        Map<String,Object> errorResponse = new HashMap<>();

        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("status",HttpStatus.FORBIDDEN.value());
        errorResponse.put("error",ACCESS_DENIED);
        errorResponse.put("message",message);
        errorResponse.put("path",path);

        return errorResponse ;
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange ,Map<String,Object> errorResponse){
        try{
            byte[] errorResponseBytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(errorResponseBytes);
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing JSON response", e);
            return exchange.getResponse().setComplete();
        }
    }


}
