package com.eventmesh.routing.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RetryPolicyService {
    private static final int MAX_RETRIES = 3;
    //In-memory Retry tracking
    private final ConcurrentHashMap<String, Integer> retryCountMap = new ConcurrentHashMap<>();

    public boolean canRetry(String eventId){
        return retryCountMap.getOrDefault(eventId, 0) < MAX_RETRIES;
    }

    public void incrementRetryCount(String eventId){
        retryCountMap.put(eventId, retryCountMap.getOrDefault(eventId, 0) + 1);
    }

    public int getRetryCount(String eventId){
        return retryCountMap.getOrDefault(eventId, 0);
    }

    public void resetRetryCount(String eventId){
        retryCountMap.remove(eventId);
    }

}
