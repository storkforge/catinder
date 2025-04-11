package org.example.springboot25.DTO;

import java.time.OffsetDateTime;

public class EventInputDTO {

    private String eventName;
    private String eventDescription;
    private String eventLocation;
    private OffsetDateTime eventDateTime;
    private Long userId;


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public OffsetDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(OffsetDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
