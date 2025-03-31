package org.example.springboot25.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_participant_event_id")
    @JsonBackReference
    private Event event;

    @Version
    private int version;

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
