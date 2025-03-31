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
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_participant_event_id")
    private Event event;


    public Long getEventParticipantId() {
        return eventParticipantId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
