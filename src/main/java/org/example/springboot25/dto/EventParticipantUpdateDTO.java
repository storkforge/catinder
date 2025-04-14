package org.example.springboot25.dto;

import java.util.Objects;

public class EventParticipantUpdateDTO {
    private String userName;
    private String eventName;

    public EventParticipantUpdateDTO() {
    }

    public EventParticipantUpdateDTO(String userName, String eventName) {
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
        if (!(o instanceof EventParticipantUpdateDTO)) return false;
        EventParticipantUpdateDTO that = (EventParticipantUpdateDTO) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(eventName, that.eventName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, eventName);
    }


    @Override
    public String toString() {
        return "EventParticipantUpdateDTO{" +
                "userName='" + userName + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }

}
