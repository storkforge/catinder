package org.example.springboot25.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class EventParticipantInputDTO {

    @NotBlank(message = "User name cannot be blank")
    private String userName;

    @NotBlank(message = "Event name cannot be blank")
    private String eventName;

    public EventParticipantInputDTO() {
    }

    public EventParticipantInputDTO(String userName, String eventName) {
        this.userName = userName;
        this.eventName = eventName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventParticipantInputDTO)) return false;
        EventParticipantInputDTO that = (EventParticipantInputDTO) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(eventName, that.eventName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, eventName);
    }


    @Override
    public String toString() {
        return "EventParticipantInputDTO{" +
                "userName='" + userName + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}

