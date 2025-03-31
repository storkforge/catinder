package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.entities.EventParticipant;
import org.example.springboot25.exception.BadRequestException;
import org.example.springboot25.exception.ConflictException;
import org.example.springboot25.exception.NotFoundException;
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

    public List<EventParticipant> getAllParticipants() {
        return eventParticipantRepository.findAll();
    }

    public List<EventParticipant> getParticipantsByEventId(Long eventId) {
        requiredNotNull(eventId, "Event ID");
        return eventParticipantRepository.findByEvent_EventId(eventId);
    }

    public List<EventParticipant>getEventByUserId(Long userId) {
        requiredNotNull(userId, "User ID");
        return eventParticipantRepository.findByUser_UserId(userId);
    }

    public EventParticipant addParticipant(EventParticipant participant) {
        requiredNotNull(participant, "Participant");
        requiredNotNull(participant.getUser(), "User in Participant");
        requiredNotNull(participant.getEvent(), "Event in Participant");

        Long userId = participant.getUser().getUserId();
        Long eventId = participant.getEvent().getEventId();

        requiredNotNull(userId, "User ID");
        requiredNotNull(eventId, "Event ID");

        if (eventParticipantRepository.existsByUser_UserIdAndEvent_EventId(userId, eventId)) {
            throw new ConflictException("User is already participating in this event.");
        }

        return eventParticipantRepository.save(participant);
    }

    public void deleteParticipation(Long id) {
        requiredNotNull(id, "EventParticipant ID");

        if (!eventParticipantRepository.existsById(id)) {
            throw new NotFoundException("No participant found with ID:" + id);
        }

        eventParticipantRepository.deleteById(id);
    }

    public boolean isUserParticipating(Long userId, Long eventId) {
        requiredNotNull(userId, "User ID");
        requiredNotNull(eventId, "Event ID");
        return eventParticipantRepository.existsByUser_UserIdAndEvent_EventId(userId, eventId);
    }

    public Optional<EventParticipant> getParticipant(Long userId, Long eventId) {
        requiredNotNull(userId, "User ID");
        requiredNotNull(eventId, "Event ID");
        return eventParticipantRepository.findByUser_UserIdAndEvent_EventId(userId, eventId);
    }

    private void requiredNotNull(Object value, String message) {
        if (value == null) {
            throw new BadRequestException(message + " must not be null");
        }
    }

}
