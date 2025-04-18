package org.example.springboot25.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ReminderType {
    VET_APPOINTMENT,
    EVENT;

    @JsonValue
    @Override
    public String toString() {
        return switch (this) {
            case VET_APPOINTMENT -> "Veterinary Appointment";
            case EVENT -> "Event";
        };
    }

    @JsonCreator
    public static ReminderType fromString(String label) {
        return Arrays.stream(values())
                .filter(e -> e.toString().equalsIgnoreCase(label)
                        || e.name().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown ReminderType: " + label));
    }
}
