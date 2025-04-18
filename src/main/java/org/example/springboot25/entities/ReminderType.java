package org.example.springboot25.entities;

public enum ReminderType {
    VET_APPOINTMENT,
    EVENT;
//visar alternativ namn
    @Override
    public String toString() {
        return switch (this) {
            case VET_APPOINTMENT -> "Veterinary Appointment";
            case EVENT -> "Event";
        };
    }
}
