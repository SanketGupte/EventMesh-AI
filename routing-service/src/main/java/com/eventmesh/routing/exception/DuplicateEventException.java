package com.eventmesh.routing.exception;

public class DuplicateEventException  extends RuntimeException{
    public  DuplicateEventException(String eventId){
        super("Duplicate Event detected for eventId: " + eventId);
    }
}
