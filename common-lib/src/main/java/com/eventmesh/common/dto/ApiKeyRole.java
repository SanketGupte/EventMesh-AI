package com.eventmesh.common.dto;

/**
 * API Key Role enumeration for role-based access control.
 */
public enum ApiKeyRole {
    ADMIN("Administrator with full access"),
    VIEWER("Read-only access to event logs"),
    PUBLISHER("Can publish events only");

    private final String description;

    ApiKeyRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

