package org.example.springboot25.dto;

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
}
