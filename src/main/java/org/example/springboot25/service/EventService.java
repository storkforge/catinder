package org.example.springboot25.service;

import org.example.springboot25.entities.Event;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final EventRepository eventRepository;

    // Konstruktor injectar repository
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Hämta alla events från databasen
    @Transactional
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Hämta ett specifikt event
    @Transactional
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event med id " + id + " not found"));
    }

    // Skapa ett nytt event och spara i databasen
    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Ta bort ett event
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event med id " + id + " not found"));
        eventRepository.delete(event);
    }

    // Uppdaterar ett event
    @Transactional
    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event med id " + id + " not found"));

        existing.setEventName(updatedEvent.getEventName());
        existing.setEventDescription(updatedEvent.getEventDescription());
        existing.setEventLocation(updatedEvent.getEventLocation());
        existing.setEventDateTime(updatedEvent.getEventDateTime());

        return eventRepository.save(existing);
    }

    // Deluppdaterar ett event
    @Transactional
    public Event patchEvent(Long id, Map<String, Object> updates) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event med id " + id + " not found"));

        if (updates.get("eventName") instanceof String name) {
            existing.setEventName(name);
        }

        if (updates.get("eventDescription") instanceof String description) {
            existing.setEventDescription(description);
        }

        if (updates.get("eventLocation") instanceof String location) {
            existing.setEventLocation(location);
        }

        if (updates.get("eventDateTime") instanceof String dateTimeStr) {
            try {
                existing.setEventDateTime(LocalDateTime.parse(dateTimeStr));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Ogiltigt datumformat för 'eventDateTime'. Använd ISO 8601-format, t.ex. 2025-04-01T14:00");
            }
        }

        return eventRepository.save(existing);
    }
}
