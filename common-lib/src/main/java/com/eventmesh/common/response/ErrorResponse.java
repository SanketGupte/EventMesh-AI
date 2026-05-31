package com.eventmesh.common.response;

import com.eventmesh.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String status;  //ERROR
    private String message;  // Human-readable message
    private String errorCode;  // INTERNAL_ERROR, AUTH_FAILED, VALIDATION_FAILED
    private Object details;  //Optional additional Details
    private LocalDateTime timestamp;

    /*Generic error*/
    public static ErrorResponse of(String message, ErrorCode errorCode){
        return ErrorResponse.builder()
                .status("ERROR")
                .message(message)
                .errorCode(errorCode.getCode())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /*Error with Details*/
    public static ErrorResponse of(String message, String errorCode, Object details){
        return ErrorResponse.builder()
                .status("ERROR")
                .message(message)
                .errorCode(errorCode)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
