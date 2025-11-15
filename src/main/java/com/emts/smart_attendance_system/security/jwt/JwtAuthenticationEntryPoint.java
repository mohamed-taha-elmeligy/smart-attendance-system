package com.emts.smart_attendance_system.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * *******************************************************************
 * File: JwtAuthenticationEntryPoint.java
 * Package: com.emts.smart_attendance_system.security.jwt
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 28/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private final ObjectMapper objectMapper ;
    private static final String AUTHENTICATION_FAILED ="Authentication Failed";
    private static final String MESSAGE = "Invalid or missing authentication token";


    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.warn("{}: {}",AUTHENTICATION_FAILED,MESSAGE);

        if (exchange.getResponse().isCommitted()) {
            return Mono.empty();
        }

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> errorMessage = createErrorResponse(
                ex.getMessage() != null ? ex.getMessage() : MESSAGE,
                exchange.getRequest().getPath().toString()
        );

        return writeResponse(exchange,errorMessage);
    }

    private Map<String,Object> createErrorResponse(String message ,String path){
        Map<String,Object> errorMessage = new HashMap<>();

        errorMessage.put("timestamp", Instant.now().toString());
        errorMessage.put("status",HttpStatus.UNAUTHORIZED.value());
        errorMessage.put("error",AUTHENTICATION_FAILED);
        errorMessage.put("message",message);
        errorMessage.put("path",path);
        return errorMessage ;
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange ,Map<String,Object> errorMessage){
        try {
            byte[] errorMessageByte = objectMapper.writeValueAsBytes(errorMessage);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(errorMessageByte);
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing JSON response", e);
            return exchange.getResponse().setComplete();
        }
    }
}
