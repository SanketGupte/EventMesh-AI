package com.eventmesh.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T>{
    private String status; //SUCCESS/ERROR
    private String message; //Human Readable Message
    private T data; //Actual Response Payload
    private LocalDateTime timestamp;

    /*Success Response*/
    public static <T> ApiResponse<T> success(String message, T data){
        return ApiResponse.<T>builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /*Success without Data*/
    public static <T> ApiResponse<T> success(String message){
        return  ApiResponse.<T>builder()
                .status("SUCCESS")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /*Error Response*/
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
