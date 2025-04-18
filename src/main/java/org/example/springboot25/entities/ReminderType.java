package org.example.springboot25.entities;

public enum ReminderType {
    VET_APPOINTMENT,
    EVENT;
//visar alternativ namn
    @Override
    public String toString() {
        switch (this) {
            case VET_APPOINTMENT:
                return "Veterinary Appointment";
            case EVENT:
                return "Event";
            default:
                return name();
        }
    }
}
