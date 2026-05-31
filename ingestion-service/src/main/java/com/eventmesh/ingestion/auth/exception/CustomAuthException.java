package com.eventmesh.ingestion.auth.exception;

public class CustomAuthException extends Exception {
    public CustomAuthException(String message) {
        super(message);
    }

    public CustomAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
