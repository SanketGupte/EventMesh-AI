package com.eventmesh.common.security;

import com.eventmesh.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthEntryPoint {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private AuthEntryPoint() {
        // Utility class
    }

    /**
     * Handle Authentication Failure (401)
     * Example: Missing / Invalid API Key
     */
    public static void handleUnauthorized(HttpServletRequest request,
                                          HttpServletResponse response,
                                          String message) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.of(
                message,
                "AUTHENTICATION_FAILED"
        );

        writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, errorResponse);
    }

    /**
     * Handle Authorization Failure (403)
     * Example: Valid API Key but insufficient role
     */
    public static void handleForbidden(HttpServletRequest request,
                                       HttpServletResponse response,
                                       String message) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.of(
                message,
                "ACCESS_DENIED"
        );

        writeResponse(response, HttpServletResponse.SC_FORBIDDEN, errorResponse);
    }

    /**
     * Common method to write response
     */
    private static void writeResponse(HttpServletResponse response,
                                      int status,
                                      ErrorResponse errorResponse) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
