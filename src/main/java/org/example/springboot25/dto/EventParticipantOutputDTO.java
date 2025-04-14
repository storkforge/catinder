package org.example.springboot25.dto;

import java.util.Objects;

public class EventParticipantOutputDTO {
    private Long id;
    private String userName;
    private String eventName;

    public EventParticipantOutputDTO() {}

    public EventParticipantOutputDTO(Long id, String userName, String eventName) {
        this.id = id;
        this.userName = userName;
        this.eventName = eventName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof EventParticipantOutputDTO that)) return false;
        return Objects.equals(id, that.id) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(eventName, that.eventName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, eventName);
    }

    @Override
    public String toString() {
        return "EventParticipantOutputDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}

