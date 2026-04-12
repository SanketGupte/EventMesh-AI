package com.eventmesh.routing.service;

import org.springframework.stereotype.Service;

@Service
public class BackoffStrategy {
    private static final long BASE_DELAY_MS = 2000; //2 seconds

    public long getBackoffDelay(int retryCount){
        return (long) (BASE_DELAY_MS  * Math.pow(2, retryCount - 1));
    }
}
