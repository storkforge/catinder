package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.entities.Event;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.ConflictException;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.EventParticipantRepository;
import org.example.springboot25.repository.EventRepository;
import org.example.springboot25.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class EventParticipantService {

    private final EventParticipantRepository eventParticipantRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EventParticipantService(EventParticipantRepository eventParticipantRepository,
                                   UserRepository userRepository,
                                   EventRepository eventRepository) {
        this.eventParticipantRepository = eventParticipantRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public List<EventParticipant> getAllParticipants() {
        return eventParticipantRepository.findAll();
    }

    public List<EventParticipant> getParticipantsByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User not found: " + userName));
        return eventParticipantRepository.findByUser(user);
    }

    public List<EventParticipant> getParticipantsByEventName(String eventName) {
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventName));
        return eventParticipantRepository.findByEvent(event);
    }

    public EventParticipant getParticipant(String userName, String eventName) {
        return eventParticipantRepository.findByUser_UserNameAndEvent_EventName(userName, eventName)
                .orElseThrow(() -> new NotFoundException("Participant not found"));
    }

    public EventParticipant getParticipantById(Long eventParticipantId) {
        return eventParticipantRepository.findById(eventParticipantId)
                .orElseThrow(() -> new NotFoundException("EventParticipant not found with ID: " + eventParticipantId));
    }

    public EventParticipant addParticipant(String userName, String eventName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User not found: " + userName));
        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventName));

        if (eventParticipantRepository.existsByUserAndEvent(user, event)) {
            throw new ConflictException("You're already participating in this event.");
        }

        return eventParticipantRepository.save(new EventParticipant(user, event));
    }

    public EventParticipant patchEventForParticipant(String userName, String newUserName, String eventName, String newEventName) {
        EventParticipant participant = getParticipant(userName, eventName);

        User newUser = userRepository.findByUserName(newUserName)
                .orElseThrow(() -> new NotFoundException("New username not found: " + newUserName));

        Event newEvent = eventRepository.findByEventName(newEventName)
                .orElseThrow(() -> new NotFoundException("New eventname not found: " + newEventName));

        participant.setEvent(newEvent);
        return eventParticipantRepository.save(participant);
    }

    public EventParticipant updateParticipant(Long eventParticipantId, String userName, String eventName) {
        EventParticipant participant = eventParticipantRepository.findById(eventParticipantId)
                .orElseThrow(() -> new NotFoundException("EventParticipant not found with ID: " + eventParticipantId));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User not found: " + userName));

        Event event = eventRepository.findByEventName(eventName)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventName));

        Optional<EventParticipant> existingParticipant = eventParticipantRepository.findByUserAndEvent(user, event);
        if (existingParticipant.isPresent() && !existingParticipant.get().getEventParticipantId().equals(eventParticipantId)) {
            throw new ConflictException("User is already participating in this event.");
        }

        participant.setUser(user);
        participant.setEvent(event);

        return eventParticipantRepository.save(participant);
    }

    public void deleteParticipant(String userName, String eventName) {
        EventParticipant participant = getParticipant(userName, eventName);
        eventParticipantRepository.delete(participant);
    }

    public void deleteParticipantById(String eventParticipantId) {
        try {
            Long id = Long.parseLong(eventParticipantId);

            EventParticipant participant = eventParticipantRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("EventParticipant not found with ID: " + id));

            eventParticipantRepository.delete(participant);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid event participant ID format: " + eventParticipantId, e);
        }
    }


}
