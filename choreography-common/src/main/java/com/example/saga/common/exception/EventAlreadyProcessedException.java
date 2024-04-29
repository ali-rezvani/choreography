package com.example.saga.common.exception;

public class EventAlreadyProcessedException extends RuntimeException{

    public static final String MESSAGE="The Event Already Processed";

    public EventAlreadyProcessedException() {
        super(MESSAGE);
    }
}
