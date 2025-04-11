package org.example.springboot25.dto;

import java.time.OffsetDateTime;

public class EventOutputDTO {

    private Long eventId;
    private String eventName;
    private String eventDescription;
    private String eventLocation;
    private OffsetDateTime eventDateTime;
    // private UserOutputDTO userEventPlanner;


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

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

    // public UserOutputDTO getUserEventPlanner() {
       // return userEventPlanner;
    //}

//    public void setUserEventPlanner(UserOutputDTO userEventPlanner) {
//        this.userEventPlanner = userEventPlanner;
//    }
}
