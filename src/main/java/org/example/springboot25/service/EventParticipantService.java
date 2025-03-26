package org.example.springboot25.service;


import jakarta.transaction.Transactional;
import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.entities.User;
import org.example.springboot25.repository.EventParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventParticipantService {

    EventParticipantRepository eventParticipantRepository;

    public EventParticipantService(EventParticipantRepository eventParticipantRepository) {
        this.eventParticipantRepository = eventParticipantRepository;
    }

    public List<EventParticipant> getParticipantsByEvent(Event event) {
        if (event == null)
            throw new IllegalArgumentException("Event must not be null");
        return eventParticipantRepository.findByEventParticipantEvent(event);
    }

    public List<EventParticipant> getParticipantsByEventId(Long eventId) {
        if(eventId == null)
            throw new IllegalArgumentException("Event ID must not be null");
        return eventParticipantRepository.findByEventParticipantEvent_EventId(eventId);
    }

    public List<EventParticipant> getEventsByUser(User user) {
        if(user == null)
            throw new IllegalArgumentException("User must not be null");
        return eventParticipantRepository.findByEventParticipantUser(user);
    }

    public List<EventParticipant> getEventById(Long userId) {
        if(userId == null)
            throw new IllegalArgumentException("User ID must not be null");
        return eventParticipantRepository.findByEventParticipantUser_UserId(userId);
    }

    public boolean isUserParticipating(Long userId, Long eventId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID must not be null");
        }
        return eventParticipantRepository.existsByEventParticipantUser_UserIdAndEventParticipantEvent_EventId(userId, eventId);
    }

    public Optional<EventParticipant> getParticipation(User user, Event event) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null");
        }
        return eventParticipantRepository.findByEventParticipantUserAndEventParticipantEvent(user, event);
    }

    public EventParticipant addParticipant(EventParticipant participant) {
        if (participant == null) {
            throw new IllegalArgumentException("Participant must not be null");
        }

        User user = participant.getUserEventParticipant();
        Event event = participant.getEventParticipantEvent();

        if (user == null) {
            throw new IllegalArgumentException("Participant must have a User");
        }
        if (event == null) {
            throw new IllegalArgumentException("Participant must have an Event");
        }

        Long userId = user.getUserId();
        Long eventId = event.getEventId();

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID must not be null");
        }

        if (isUserParticipating(userId, eventId)) {
            throw new IllegalStateException("User is already participating in this event");
        }

        return eventParticipantRepository.save(participant);
    }

    public void deleteParticipant(Long eventParticipantId) {
        if (eventParticipantId == null) {
            throw new IllegalArgumentException("EventParticipant ID must not be null");
        }

        if (!eventParticipantRepository.existsById(eventParticipantId)) {
            throw new IllegalArgumentException("No participant found with ID: " + eventParticipantId);
        }

        eventParticipantRepository.deleteById(eventParticipantId);
    }

    public List<EventParticipant> getAllParticipants() {
        return eventParticipantRepository.findAll();
    }

}
