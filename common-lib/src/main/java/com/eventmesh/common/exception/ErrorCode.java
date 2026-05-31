package com.eventmesh.common.exception;

public enum ErrorCode {
    // Authentication / Authorization
    AUTHENTICATION_FAILED("AUTH_001"),
    ACCESS_DENIED("AUTH_002"),

    // Validation
    VALIDATION_FAILED("VAL_001"),

    // Business / Routing
    ROUTING_RULE_NOT_FOUND("BUS_001"),
    EVENT_PROCESSING_FAILED("BUS_002"),

    // Database / Idempotency
    DUPLICATE_EVENT("DB_001"),
    DATABASE_ERROR("DB_002"),

    // Generic
    INTERNAL_ERROR("GEN_001");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
