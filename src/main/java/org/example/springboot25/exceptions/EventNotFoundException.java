package org.example.springboot25.exceptions;

import org.example.springboot25.exceptions.EventNotFoundException;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
