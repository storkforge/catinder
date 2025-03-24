package org.example.springboot25.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class EventParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventParticipantId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_participant_user_id")
    private User userEventParticipant;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_participant_event_id")
    private Event eventParticipantEvent;


    public Long getEventParticipantId() {
        return eventParticipantId;
    }

    public @NotNull User getUserEventParticipant() {
        return userEventParticipant;
    }

    public void setUserEventParticipant(@NotNull User userEventParticipant) {
        this.userEventParticipant = userEventParticipant;
    }

    public @NotNull Event getEventParticipantEvent() {
        return eventParticipantEvent;
    }

    public void setEventParticipantEvent(@NotNull Event eventParticipantEvent) {
        this.eventParticipantEvent = eventParticipantEvent;
    }

}
