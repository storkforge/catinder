package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotBlank
    private String eventName;

    @NotBlank
    private String eventDescription;

    @NotBlank
    private String eventLocation;

    @NotNull
    @FutureOrPresent
    private LocalDateTime eventDateTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_planner_user_id")
    private User userEventPlanner;

    @OneToMany(mappedBy = "eventParticipantEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipant> eventParticipants;


    public Long getEventId() {
        return eventId;
    }

    public @NotBlank String getEventName() {
        return eventName;
    }

    public void setEventName(@NotBlank String eventName) {
        this.eventName = eventName;
    }

    public @NotBlank String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(@NotBlank String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public @NotBlank String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(@NotBlank String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public @NotNull @FutureOrPresent LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(@NotNull @FutureOrPresent LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public @NotNull User getUserEventPlanner() {
        return userEventPlanner;
    }

    public void setUserEventPlanner(@NotNull User userEventPlanner) {
        this.userEventPlanner = userEventPlanner;
    }

    public List<EventParticipant> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(List<EventParticipant> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

}
