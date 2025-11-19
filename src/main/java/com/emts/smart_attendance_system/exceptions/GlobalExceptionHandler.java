package com.emts.smart_attendance_system.exceptions;

import com.emts.smart_attendance_system.exceptions.exception.CurrentDeleteException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * *******************************************************************
 * File: null.java
 * Package: com.emts.smart_attendance_system.exceptions
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 31/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Response
    @ExceptionHandler(CurrentDeleteException.class)
    public Mono<ResponseEntity<Map<String,String>>> handelCurrentDeleteException (CurrentDeleteException ex){
        Map<String, String> response = Map.of(
                "status", "error",
                "message", ex.getMessage()
        );
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response));
    }





    // Resource Not Found (404)
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleNotFound(ChangeSetPersister.NotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage())));
    }

    // Duplicate or conflict (409)
    @ExceptionHandler(DuplicateException.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleDuplicate(DuplicateException ex) {
        log.warn("Duplicate resource: {}", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(HttpStatus.CONFLICT, ex.getMessage())));
    }

    // Validation error (400)
    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleValidation(ValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage())));
    }

    // MethodArgumentNotValidException (مثلاً من @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleInvalidArguments(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("Invalid input: {}", errorMessage);
        assert errorMessage != null;
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage)));
    }

    // Business logic error (custom 422)
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleBusiness(BusinessException ex) {
        log.error("Business rule violated: {}", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(422)
                .body(new ApiErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage())));
    }

    // Fallback for all unexpected exceptions (500)
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiErrorResponse>> handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error")));
    }

}
