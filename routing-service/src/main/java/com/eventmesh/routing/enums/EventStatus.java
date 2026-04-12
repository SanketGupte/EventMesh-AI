package com.eventmesh.routing.enums;

import java.util.Arrays;

public enum EventStatus {
    RECEIVED,
    ROUTED,
    FAILED,
    RETRIED,
    FAILED_PERMANENT,
    DLQ,
    RETRY_SUCCESS;

    /*
    * Safe Conversion from String to enum
    * prevents IllegalArgumentException crashes
    */
    public static  EventStatus from(String value){
        return Arrays
                .stream(EventStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid EventStatus: " + value));
    }

    /*
    * Safe conversion with fallback (useful for backward compactibility)
    */
    public static  EventStatus fromOrDefault(String value, EventStatus defaultStatus){
        try{
            return from(value);
        } catch (Exception e){
            return defaultStatus;
        }
    }

}
