package com.eventmesh.ingestion.exception;

import com.eventmesh.common.exception.ErrorCode;
import com.eventmesh.common.response.ErrorResponse;
import com.eventmesh.ingestion.auth.exception.CustomAuthException;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomAuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(CustomAuthException ex) {
        String correlationId = MDC.get("correlationId");
        logger.warn("Authentication failure: {} correlationId={}", ex.getMessage(), correlationId);

        return ResponseEntity.status(403).body(
                ErrorResponse.of(ex.getMessage(), ErrorCode.ACCESS_DENIED)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidation(IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(
                ErrorResponse.of(ex.getMessage(), ErrorCode.VALIDATION_FAILED)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        String errorId = UUID.randomUUID().toString();
        String correlationId = MDC.get("correlationId");

        logger.error("Unhandled exception errorId={} correlationId={}", errorId, correlationId, ex);

        String userMessage = "Unexpected internal error occurred. Reference id: " + errorId;

        return ResponseEntity.status(500).body(
                ErrorResponse.of(userMessage, ErrorCode.INTERNAL_ERROR)
        );
    }
}
