package com.eventmesh.ingestion.exception;

import com.eventmesh.common.exception.ErrorCode;
import com.eventmesh.common.response.ErrorResponse;
import com.eventmesh.ingestion.auth.exception.CustomAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomAuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(CustomAuthException ex) {

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

        return ResponseEntity.status(500).body(
                ErrorResponse.of(ex.getMessage(), ErrorCode.INTERNAL_ERROR)
        );
    }
}
