package org.example.springboot25.exceptions;

public class ReminderNotFoundException extends RuntimeException {
    public ReminderNotFoundException(String message) {
        super(message);
    }

    public ReminderNotFoundException(Long id) {
        super("Påminnelse med id " + id + " hittades inte");
    }

    public ReminderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
